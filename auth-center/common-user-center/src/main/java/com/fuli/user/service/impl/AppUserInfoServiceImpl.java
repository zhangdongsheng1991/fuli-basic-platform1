package com.fuli.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.auth.common.model.BaseUser;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseServiceImpl;
import com.fuli.user.configuration.service.ProductionAccessTokenService;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.dao.*;
import com.fuli.user.dto.*;
import com.fuli.user.exception.BusinessException;
import com.fuli.user.feign.BasicServerFeign;
import com.fuli.user.feign.MiddleServerFeign;
import com.fuli.user.model.*;
import com.fuli.user.service.AppUserInfoService;
import com.fuli.user.service.RealNameInTaiwanService;
import com.fuli.user.utils.*;
import com.fuli.user.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @Description: TODO
 * @Author: WS
 * @CreateDate: 2019/4/18 19:22
 * @Version: 1.0
 */
@Service
@Slf4j
public class AppUserInfoServiceImpl extends BaseServiceImpl<AppUserDao, AppUser> implements AppUserInfoService {

    @Autowired
    private AppUserInfoDao appUserInfoDao;
    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private ProductionAccessTokenService productionAccessTokenService;
    @Autowired
    private RealNameInTaiwanService realNameInTaiwanService;
    @Autowired
    private SaAsDefaultCompanyMapper saAsDefaultCompanyMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DataRecordUserMapper dataRecordUserMapper;
    @Autowired
    private DataRecordEmployeeMapper dataRecordEmployeeMapper;
    @Autowired
    private MiddleServerFeign middleServerFeign;
    @Autowired
    private RedisService redisService;

    /**
     * app用户获取用户信息，带企业信息  2019-08-07 修改
     * @author      WFZ
     * @param 	    id : 用户id
     * @return      Result
     * @date        2019/8/7 14:57
     */
    @Override
    public Result getUserInfo(Long id) {
        AppUser appUser = baseMapper.selectById(id);
        if(null == appUser){
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT);
        }
        /** 密码不返回*/
        appUser.setPassword(null);
        appUser.setPayPassword(null);
        if(StringUtils.isBlank(appUser.getRealName())){
            appUser.setRealName("暂无");
        }
        /** 根据用户id获取企业列表*/
        List<CompanyByUserIdVO> companyList = baseMapper.listCompanyByUserId(appUser.getId());
        if(PublicUtil.isNotEmpty(companyList)){
            /** 记录是否选择默认企业*/
            boolean flag = true ;
            for (CompanyByUserIdVO vo : companyList){
                if (vo.getIsShow().equals("1")){
                    flag = false ;
                }
            }
            if (flag){
                companyList.get(0).setIsShow("1");
            }
            appUser.setCompanys(companyList);
        }
        try{
            String middleUserId ="";
            String key = CommonConstant.MIDDLE_USER_ID + appUser.getId();
            if (redisService.exists(key)){
                middleUserId = String.valueOf(redisService.get(key));
            }else {
                /** 获取中台用户Id*/
                Result result = middleServerFeign.findOtherUserID(String.valueOf(appUser.getId()));
                log.info("请求middle服务获取中台用户result:{}",result);
                if (PublicUtil.isResultSuccess(result) && PublicUtil.isNotEmpty(result.getData())){
                    middleUserId = String.valueOf(result.getData());
                    redisService.set(key,middleUserId,3600L);
                }
            }
            appUser.setMiddleUserId(middleUserId);
            log.info("请求middle服务获取中台用户id:{}",middleUserId);
        }catch (Exception e){
            log.error("请求middle服务获取中台用户id报错",e);
        }
        return Result.succeed(appUser);
    }


    /**
     * 用户切换企业，重新生成token
     * @author      fengjing
     * @param       request : 请求类
     * @param       userInfoVO  : 登录用户信息
     * @return      Result
     * @date        2019/6/21 11:21
     */
    @Override
    public Result switchCompany(UserSwitchCompanyDTO request, TokenUserVO userInfoVO) {
        Result accessToken = Result.failed();
        /** 根据用户id和企业id获取用户信息*/
        AppUserVo appUserVo = baseMapper.listCompanyAndUserById(userInfoVO.getId(), Long.valueOf(request.getCurrentCompanyId()));
        if (null==appUserVo){
            return Result.failed("非法请求");
        }
        String clientId = CommonConstant.CLIENT_APP.equals(request.getClient_id()) ? CommonConstant.CLIENT_ID:request.getClient_id();
        BaseUser jwtDTO = new BaseUser();
        jwtDTO.setPhone(appUserVo.getPhone());
        /** 如果是app默认转成 shuKeApp*/
        jwtDTO.setClientId(clientId);
        jwtDTO.setRealName(appUserVo.getRealName());
        jwtDTO.setUserId(appUserVo.getId().toString());
        jwtDTO.setUserAccount(appUserVo.getUsername());
        jwtDTO.setEmployeeId(appUserVo.getEmployeeId());
        jwtDTO.setCurrentCompanyId(appUserVo.getCompanyId());
        jwtDTO.setCompanyCreditCode(appUserVo.getCompanyCreditCode());
        try{
            /** 生产token*/
            accessToken = productionAccessTokenService.createAccessToken(clientId, CommonConstant.CLIENT_SECRET_SWITCHOVER, CommonConstant.CLIENT_GRANT_TYPE, jwtDTO,1);
            if (PublicUtil.isResultSuccess(accessToken)){
                if (CommonConstant.CLIENT_ID_PORTAL.equals(request.getClient_id())){
                    /** 如果是SaaS门户切换token，需要在员工默认展示企业表添加数据*/
                    if ( ! PublicUtil.isNotNull(userInfoVO.getEmployeeId())){
                        return Result.failed(CodeEnum.ACCESS_TOKEN_NULLITY);
                    }
                    saAsDefaultCompanyMapper.deleteById(userInfoVO.getEmployeeId());
                    saAsDefaultCompanyMapper.deleteById(Long.valueOf(appUserVo.getEmployeeId()));
                    SaAsDefaultCompanyDO defaultCompanyDO = new SaAsDefaultCompanyDO();
                    defaultCompanyDO.setCompanyId(Long.valueOf(request.getCurrentCompanyId()));
                    defaultCompanyDO.setEmployeeId(Long.valueOf(appUserVo.getEmployeeId()));
                    saAsDefaultCompanyMapper.insert(defaultCompanyDO);

                }else if (CommonConstant.CLIENT_ID.equals(request.getClient_id()) || CommonConstant.CLIENT_APP.equals(request.getClient_id())){
                    /** app用户切换token需要修改默认展示的企业*/
                    baseMapper.removeEmployeeIsShowByUserId(userInfoVO.getId());
                    baseMapper.updateEmployeeIsShowByUserIdAndCompanyId(Long.valueOf(appUserVo.getEmployeeId()));
                }
                return accessToken;
            }
        }catch (Exception e){
            log.error("切换企业获取token出错",e);
        }
        return Result.failed(CodeEnum.GET_ACCESS_TOKEN_FAIL);
    }

    /**
     * 实名校验，并修改实名状态
     * @author      WFZ
     * @param 	    dto ：请求类
     * @return      Result
     * @date        2019/8/7 16:24
     */
    @Override
    @Transactional(rollbackFor = {BusinessException.class})
    public Result updateRealNameStatus(UserCertificationDTO dto) {
        /** 获取用户信息*/
        AppUser user = baseMapper.selectById(dto.getUserId());
        if (null == user){
            return Result.failed("用户不存在");
        }else if (user.getIsIdentification().intValue()==1 && StringUtils.isNotBlank(user.getCertificateCard())){
            log.info("用户实名认证，不能重复实名");
            return Result.succeed("用户已实名");
        }
        /** 如果有员工id, 则要校验员工表里面的身份证和姓名*/
        EmployeeDO employeeDO = null;
        if (PublicUtil.isNotNull(dto.getEmployeeId())){
            employeeDO = employeeMapper.selectById(dto.getEmployeeId());
        }else {
            QueryWrapper<EmployeeDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id,company_id,name,phone_number,certificate_card");
            queryWrapper.apply("user_id = "+dto.getUserId()+" AND certificate_card != '' LIMIT 1");
            List<EmployeeDO> list = employeeMapper.selectList(queryWrapper);
            if (! CollectionUtils.isEmpty(list)){
                employeeDO = list.get(0);
            }
        }
        if (employeeDO != null){
            if (StringUtils.isNotBlank(employeeDO.getName()) && ! dto.getName().equals(employeeDO.getName())){
                return Result.failed("姓名与员工信息不一致，请修改姓名或联系企业管理员");
            }
            if (StringUtils.isNotBlank(employeeDO.getCertificateCard()) && ! dto.getIdNum().equals(employeeDO.getCertificateCard())){
                return Result.failed("身份证号与员工信息不一致，请修改身份证号或联系企业管理员");
            }
            /** 判断同企业下是否有相同的身份证号*/
            EmployeeDO employee = employeeMapper.getEmployeeByIdCard(dto.getIdNum(), dto.getUserId());
            if (employee != null && String.valueOf(employee.getCompanyId()).equals(String.valueOf(employeeDO.getCompanyId())) ){
                return Result.failed(CodeEnum.IDENTITY_INFORMATION_REAL_NAME.getCode(),"该身份证号已实名认证，您可以使用手机号"+employee.getPhoneNumber() + "登录");
            }
        }
        /** 三要素校验身份证号码是否真实*/
        RealNameInTaiwanDTO nameDto = new RealNameInTaiwanDTO();
        nameDto.setIdNum(dto.getIdNum());
        nameDto.setName(dto.getName());
        nameDto.setPhoneNumber(dto.getPhoneNumber());
        Result result = realNameInTaiwanService.obtainRealName(nameDto);
        if (! PublicUtil.isResultSuccess(result)){
            return result;
        }

        /** 将用户名转为拼音,并判断是否唯一，不是的话加1 */
        String systemAccount = SpellHelperUtil.toPinyin(dto.getName());
        if ( StringUtils.isNotBlank(systemAccount)){
            if (StringUtils.isBlank(user.getUsername()) || ! user.getUsername().contains(systemAccount)){
                Integer count = baseMapper.getCountByLikeUserName(systemAccount);
                if(PublicUtil.isNotNull(count)){
                    systemAccount = systemAccount + (count + 1);
                }
                user.setUsername(systemAccount);
            }
        }
        /** 更改数据库数据*/
        user.setIsIdentification(1);
        user.setRealName(dto.getName());
        user.setCertificateType(1);
        user.setCertificateCard(dto.getIdNum());
        user.setGender(RegexUtil.getCarInfo(dto.getIdNum()).get("sex"));
        user.setImgPositive(dto.getImgPositive());
        user.setImgReverse(dto.getImgReverse());
        user.setUpdateTime(new Date());
        int i = baseMapper.updateById(user);
        if (i != 1){
            return Result.failed();
        }

        /** 数据同步中台*/
        return dataSynchronizationCenter(user);

    }

    /**
     * 实名认证通过后数据同步到中台 -- TODO 保存到中间表 2019-10-14 修改
     * @author      WFZ
     * @param 	    user ： 用户信息
     * @return      Result
     * @date        2019/8/7 17:17
     */
    public Result dataSynchronizationCenter(AppUser user){
        /** 防止并发,防止死锁加一个时间10秒*/
        String key = "sync_data_user_authentication: |" + user.getId();
        boolean successFlag = RedisLockUtil.tryLock(key,1,10);
        if (!successFlag){
            return Result.succeed();
        }

        /** 如果员工的身份证号码为空，用户实名后同步身份证号码到员工*/
        int i1 = employeeMapper.updateEmployeeIdCard(user.getCertificateCard(), user.getGender(),user.getRealName() , user.getId());
        log.info("用户实名认证同步修改的员工数：{}",i1);
        try{
            log.info("实名认证通过后数据同步到中台");
            DataRecordUserDO data = new DataRecordUserDO();
            data.setUserId(user.getId());
            data.setPhone(user.getPhone());
            data.setPassword(user.getPassword());
            data.setRealName(user.getRealName());
            data.setUsername(user.getUsername());
            data.setEmail(user.getEmail());
            data.setGender(user.getGender());
            data.setIsCertificated(user.getIsIdentification());
            data.setStatus(user.getThisStatus());
            data.setCertificateType(user.getCertificateType());
            data.setCertificateCard(user.getCertificateCard());
            data.setUserFrom(user.getUserFrom());
            data.setCreateTime(new Date());
            data.setType(1);
            dataRecordUserMapper.insert(data);

            /** 同步用户还需要同步员工*/
            List<DataRecordEmployeeDO> doList = employeeMapper.listEmployeeByUserId(user.getId());
            if (!CollectionUtils.isEmpty(doList)){
                log.info("用户实名后同步中台，需要同步的员工数="+doList.size());
                for (DataRecordEmployeeDO dataDo : doList){
                    dataDo.setCreateTime(new Date());
                    dataDo.setType(1);
                    dataRecordEmployeeMapper.insert(dataDo);
                }
            }
        }catch (Exception e){
            log.error("数据同步中台" , e);
            throw new BusinessException(CodeEnum.GLOBAL_EXCEPTION);
        }finally {
            /** 释放锁*/
            RedisLockUtil.unlock(key);
        }
        return Result.succeed();
    }


    /**
     * 更换手机号 - 2019-08-02 修改接口
     * @author      WFZ
     * @param 	    dto  : 请求类
     * @param 	    userInfoVO  : 登录用户信息
     * @return      Result
     * @date        2019/8/2 16:32
     */
    @Override
    @Transactional(rollbackFor = {BusinessException.class})
    public Result replacePhone(ChangePhoneDTO dto, TokenUserVO userInfoVO) {
        SmsVerifyCodeDTO vo = new SmsVerifyCodeDTO();
        vo.setMobile(dto.getNewPhone());
        vo.setCode(dto.getVerifyCode());
        vo.setSmsSendSource(CommonConstant.SMS_UPDATE_PHONE);
        Result r = smsVerifyCode(vo);
        if (! PublicUtil.isResultSuccess(r)){
            return r;
        }
        /** 验证新手机号是否已经存在*/
        AppUser user = baseMapper.findUserByPhone(dto.getNewPhone());
        if(user != null){
            return Result.failed(CodeEnum.PHONE_ALREADY_REGISTER);
        }
        AppUser appUser = new AppUser();
        appUser.setId(userInfoVO.getId());
        appUser.setPhone(dto.getNewPhone());
        int i = baseMapper.updateById(appUser);
        if(i != 1){
            return Result.failed(CodeEnum.PHONE_UPDATE_FAILED);
        }
        log.info("修改手机号插入记录到中间表phone:{},userId:{}",dto.getNewPhone(), userInfoVO.getId());
        DataRecordUserDO data = new DataRecordUserDO();
        data.setUserId(userInfoVO.getId());
        data.setPhone(dto.getNewPhone());
        data.setUsername(userInfoVO.getUserAccount());
        data.setCreateTime(new Date());
        data.setType(4);
        dataRecordUserMapper.insert(data);
        /** 同步修改在职员工的手机号*/
        int i1 = employeeMapper.updateEmployeePhoneNumber(dto.getNewPhone(), userInfoVO.getId());
        log.info("用户修改手机号同步修改在职员工的手机号影响条数：{}",i1);
        return Result.succeed(CodeEnum.PHONE_UPDATE_SUCCESS);
    }

    @Override
    public PageResultVO<List<AppUserIdsVO>> getUserIds(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<AppUserIdsVO> userIds = appUserInfoDao.getUserIds();
        PageInfo pageInfo = new PageInfo(userIds);
        return PageResultVO.getPageResult(pageInfo);
    }

    /**
     * @Description:(批量手机号查询批量id)
     * @author      fengjing
     * @date        2019/6/27 9:34
     */
    @Override
    public List<AppUserIdVO> phoneGetUserId(List<String> list) {
        return appUserInfoDao.phoneGetUserId(list);
    }

    /**
     * 根据身份证号获取用户信息
     *
     * @param idCard ： 身份证号
     * @return Result
     * @author WFZ
     * @date 2019/8/8 10:14
     */
    @Override
    public AppUser findByIdCard(String idCard) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone","real_name","gender","certificate_card","is_identification");
        queryWrapper.eq("certificate_card", idCard).apply("1=1 LIMIT 1");
        AppUser user = baseMapper.selectOne(queryWrapper);
        if (user != null){
            /** 密码不返回*/
            user.setPassword(null);
            user.setPayPassword(null);
            return user;
        }
        return null;
    }

    /**
     * 根据身份证号判断用户是否实名
     *
     * @param idCard ： 身份证号码
     * @return Result
     * @author WFZ
     * @date 2019/8/12 10:36
     */
    @Override
    public Boolean isRealNameByIdCard(String idCard) {
        /** 首先根据身份证号码和认证状态查询是否存在*/
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","phone","certificate_card","is_identification");
        queryWrapper.eq("certificate_card", idCard).eq("is_identification", 1).apply("1=1 LIMIT 1");
        AppUser user = baseMapper.selectOne(queryWrapper);
        if (user != null){
            return true;
        }
        return false;
    }


    /**
     * 封装验证码校验
     * @author      FZ
     * @param 	    dto
     * @return      Result
     * @date        2019/4/22 15:01
     */
    public Result smsVerifyCode(SmsVerifyCodeDTO dto) {
        try {
            return basicServerFeign.smsVerifyCode(dto);
        }catch (Exception e){
            log.error("调用basic-server短信验证",e);
        }
        return Result.failed("basic-server服务出错");
    }
}

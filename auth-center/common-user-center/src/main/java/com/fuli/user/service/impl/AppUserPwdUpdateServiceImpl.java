package com.fuli.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.dao.AppUserDao;
import com.fuli.user.dao.DataRecordUserMapper;
import com.fuli.user.dto.*;
import com.fuli.user.feign.BasicServerFeign;
import com.fuli.user.model.AppUser;
import com.fuli.user.model.DataRecordUserDO;
import com.fuli.user.service.AppUserPwdUpdateService;
import com.fuli.user.service.RealNameInTaiwanService;
import com.fuli.user.utils.PublicUtil;
import com.fuli.user.utils.RSAEncrypt;
import com.fuli.user.utils.RedisService;
import com.fuli.user.utils.RegexUtil;
import com.fuli.user.vo.CompanyByUserIdVO;
import com.fuli.user.vo.CompanyUserVO;
import com.fuli.user.vo.TokenUserVO;
import com.fuli.user.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @Deprecated 用户密码修改服务
 * @Author xq
 * @Date 2019/4/16
 * @Version 1.0
 */

@Service
@Slf4j
public class AppUserPwdUpdateServiceImpl implements AppUserPwdUpdateService {

    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private AppUserPwdUpdateService appUserPwdUpdateService;
    @Resource
    private RedisService redisService;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private AppUserLoginAndRegisterServiceImpl appUserLoginAndRegisterServiceImpl;
    @Autowired
    private RealNameInTaiwanService realNameInTaiwanService;
    @Value("${FULI_RSA_USER}")
    private String FULI_RSA_USER;

    /**
     * 用户查询
     *
     * @param phone 用户信息
     * @return 用户信息
     */
    @Override
    public AppUser findAppUserByPhone(String phone) {
        return appUserDao.findUserByPhone(phone);
    }

    /**
     * 修改本地用户密码
     *
     * @param request 请求参数封装
     * @param type 3:忘记密码 ，2- 修改密码
     * @return Result
     */
    @Override
    public Result updatePasswordByPhone(AppUserUpdatePasswordRequest request, int type) {
        /** 旧密码*/
        String usedPwd = request.getUsedPwd() == null ? "" : RSAEncrypt.getPassword(request.getUsedPwd(), FULI_RSA_USER);
        /** 新密码*/
        String newPassword = RSAEncrypt.getPassword(request.getPassword(), FULI_RSA_USER);
        /**如果修改的新密码与再次输入密码都不为空，检验两次输入密码是否一致。如果只传新密码则不需要校验*/
        if (StringUtils.isNotBlank(request.getRetypePassword()) && ! newPassword.equals(RSAEncrypt.getPassword(request.getRetypePassword(), FULI_RSA_USER)) ) {
            return Result.failed(CodeEnum.USER_ANGIN_PASSWORD);
        }
        /** 查询用户信息*/
        AppUser appUser = new AppUser();
        if (2 == type && PublicUtil.isNotNull(request.getUserId())){
            appUser = appUserDao.selectById(request.getUserId());
        }else{
            appUser = appUserDao.findUserByPhone(request.getPhone().trim());
        }
        if (appUser == null) {
            return Result.failed(CodeEnum.USER_NOT_FOUND_ERROR);
        }
        /** 修改密码校验旧密码*/
        if(2 == type && CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom()) && StringUtils.isNotBlank(appUser.getPassword()) && !appUser.getPassword().equals(usedPwd)){
            return Result.failed(CodeEnum.OLDNEW_PWD_ATYPISM);
        }
        /** 删除Redis中保存的密码输入错误次数 */
        redisService.remove(CommonConstant.APP_USER_PASSWORD + appUser.getId());

        PlatformPasswordDTO dto = new PlatformPasswordDTO();
        dto.setThirdUserId(appUser.getId().toString());
        dto.setPassword(newPassword);
        dto.setLoginName(request.getPhone());
        dto.setUserFrom(appUser.getUserFrom());
        // 本地用户不去中台校验旧密码
        dto.setOldPassword(CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom())? null : usedPwd);
        /** 本地用户就修改数据库*/
        if(CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom())) {
            appUser.setPassword(newPassword);
            appUser.setUpdateTime(new Date());
            int i = appUserDao.updateById(appUser);
            if (i < 1){
                log.error("用户修改密码出错========");
                return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
            }
        }
        /** 所有用户的修改密码都要同步给中台*/
        Result result = realNameInTaiwanService.syncPlatformPwd(dto);
        log.info("用户修改密码同步中台返回结果：{}",result);

        if(CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom())){
            return Result.succeed();
        }
        return result;
    }

    /**
     * 修改用户本地支付密码
     *
     * @param request 请求参数封装
     * @param tokenUserVO : 当前登录用户
     * @return Result
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public Result updatePayPasswordByPhone(AppUserUpdatePayPwdRequest request,TokenUserVO tokenUserVO) {
        if(2 == request.getType() && StringUtils.isEmpty(request.getUsedPwd()) ){
            return Result.failed(CodeEnum.PLATFORM_OLDNEW_PWD_ATYPISM);
        }
        /** 本地新支付密码*/
        String newPayPwd =RSAEncrypt.getPassword(request.getPaypwd(), FULI_RSA_USER);
        /** 校验两次输入的密码是否一样*/
        if (StringUtils.isNotBlank(request.getRetypePaypwd())  && ! newPayPwd.equals(RSAEncrypt.getPassword(request.getRetypePaypwd(), FULI_RSA_USER))) {
            return Result.failed(CodeEnum.USER_ANGIN_PASSWORD);
        }
        /** 检验账号是否存在*/
        AppUser appUser = appUserDao.selectById(tokenUserVO.getId());
        if (appUser == null) {
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT.getCode(),"当前登录状态有误，请重新登录");
        }
        /** 旧密码*/
        if(request.getType() == 2 && StringUtils.isNotBlank(appUser.getPayPassword())){
            String oldPayPwd = RSAEncrypt.getPassword(request.getUsedPwd(), FULI_RSA_USER);
            if(!appUser.getPayPassword().equals(oldPayPwd)){
                return Result.failed(CodeEnum.OLDNEW_PWD_ATYPISM);
            }
        }
        appUser.setPayPassword(newPayPwd);
        appUser.setPayPasswordFlag(1);
        appUser.setUpdateTime(new Date());
        appUserDao.updateById(appUser);

        /** 清空Redis中支付密码输错次数*/
        redisService.remove(CommonConstant.APP_PLAY_PASSWD + appUser.getId());
        return Result.succeed();
    }



    /**
     * 发送验证码
     * @author      xq
     * @param       smsSendDto 手机验证码发送封装
     * @date        2019/4/16
     */
    @Override
    public Result sendSms(SmsSendDTO smsSendDto, String phone) {
        /** 数据校验 */
        if (StringUtils.isEmpty(smsSendDto.getMobile())) {
            return Result.failed(CodeEnum.USER_NOT_MOBLIE_EMPTY);
        }
        if(! RegexUtil.find(RegexUtil.PHONE,smsSendDto.getMobile())){
            return Result.failed(CodeEnum.PHONE_FORMAT_ERROR);
        }
        /** login, register, findpwd; 发送验证码  TODO 新增绑定手机号发送验证码不验证用户是否存在binding */
        if (! CommonConstant.SMS_SEND_SOURCE_BINDING.equals(smsSendDto.getSmsSendSource()) || ! CommonConstant.SMS_SEND_SOURCE_LOGIN.equals(smsSendDto.getSmsSendSource())){
            log.info("进来了========================================================");
            AppUser user = appUserPwdUpdateService.findAppUserByPhone(smsSendDto.getMobile());
            /**修改本地密码，支付密码*/
            if(CommonConstant.SMS_SEND_SOURCE_FINDPWD.equals(smsSendDto.getSmsSendSource()) || CommonConstant.SMS_SEND_SOURCE_UPDATEPWD.equals(smsSendDto.getSmsSendSource()) || CommonConstant.SMS_SEND_SOURCE_UPDATE_PAYPWD.equals(smsSendDto.getSmsSendSource())){
                if(user == null){
                    return Result.failed(CodeEnum.USER_NOT_FOUND_ERROR);
                }
                if(!phone.equals(smsSendDto.getMobile())){
                    return Result.failed(CodeEnum.LOGIN_USER_PHONE_NOT_MASTER);
                }
            }
            /** 注册验证码*/
            if(CommonConstant.SMS_SEND_SOURCE_REGISTER.equals(smsSendDto.getSmsSendSource())){
                if(user != null){
                    return Result.failed(CodeEnum.USER_NOT_EMPTY);
                }
            }
            /**忘记密码*/
            if(CommonConstant.SMS_SEND_SOURCE_FORGET_PWD.equals(smsSendDto.getSmsSendSource())){
                if(user == null){
                    return Result.failed(CodeEnum.USER_NOT_FOUND_ERROR);
                }
            }
            /** 修改手机号 */
            if(CommonConstant.SMS_UPDATE_PHONE.equals(smsSendDto.getSmsSendSource())){
                if(user != null){
                    return Result.failed(CodeEnum.PHONE_ALREADY_REGISTER);
                }
            }
        }
        return basicServerFeign.sendSmsVerifyCode(smsSendDto);
    }


    /**
     * @Description:(中台系统修改本系统用户密码时同步)
     * @author      fengjing
     * @date        2019/6/6 10:45
     */
    @Override
    public Result syncPassword(Map<String, Object> map) {
        String id = map.get("thirdUserId") == null ? "" : map.get("thirdUserId").toString();
        String password = map.get("password") == null ? "" : map.get("password").toString();
        AppUser appUser = appUserDao.selectById(id);
        if(appUser.getUserFrom().equals(CommonConstant.FU_LI_LABEL)){
            appUser.setPassword(password);
            appUser.setUpdateTime(new Date());
            int count = appUserDao.updateById(appUser);
            if(count>0){
                return Result.succeed();
            }
            return Result.failed("同步密码失败");
        }
        return Result.failed("用户非本地用户");
    }


    /**
     * 登录密码校验
     * @author      fengjing
     * @param       map -- password : 密码
     * @param       tokenUserVO : 登录用户信息
     * @return      Result
     * @date        2019/6/11 9:26
     */
    @Override
    public Result logonPwdValidity(Map<String, Object> map, TokenUserVO tokenUserVO) {
        String password = map.get("password") + "";
        AppUser appUser = appUserDao.selectById(tokenUserVO.getId());
        if (appUser == null){
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT);
        }

        UserLoginVO userLoginVo = new UserLoginVO();
        userLoginVo.setPassword(password);
        userLoginVo.setPhone(tokenUserVO.getPhone());

        Result result = appUserLoginAndRegisterServiceImpl.passwordModeLogin(userLoginVo, appUser,2);
        if (PublicUtil.isResultSuccess(result)){
            return Result.succeed();
        }
        return result;
    }


    /**
     * 根据companyOpenId,userOpenId返回用户信息
     * @author      fengjing
     * @param       dto : companyOpenId,userOpenId
     * @return      Result
     * @date        2019/5/28 14:38
     */
    @Override
    public Result companyUserData(IousRequestDTO dto) {
        if (StringUtils.isBlank(dto.getOpenId())){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode() ,"用户唯一id不能为空");
        }
        if (StringUtils.isBlank(dto.getCompanyOpenId())){
            return Result.failed(CodeEnum.PARAM_ERROR.getCode() ,"企业唯一id不能为空");
        }

        /**查询该用户是否存在，不存在直接返回，存在则去查询相应的企业信息和员工id*/
        AppUser appUser = appUserDao.findUserByOpenId(dto.getOpenId());
        if(appUser == null){
            return Result.failed(CodeEnum.CHECKED_USER_NONEXISTENT);
        }

        CompanyUserVO vo = new CompanyUserVO();

        CompanyByUserIdVO appUserVo = appUserDao.findCompanyAndUserByOpenId(appUser.getId(), dto.getCompanyOpenId());
        if (appUserVo != null && StringUtils.isNotBlank(appUserVo.getCompanyId())){
            vo.setCompanyid(Long.valueOf(appUserVo.getCompanyId()));
            vo.setCompanyname(appUserVo.getCompanyName());
            vo.setEmpid(Long.valueOf(appUserVo.getEmployeeId()));
            log.info("企业和员工信息{}",vo.getCompanyid(),vo.getEmpid());
        }
        vo.setUserid(appUser.getId());
        vo.setUsername(appUser.getUsername());
        return Result.succeed(vo);
    }

    /**
     * 支付密码校验
     * @author      fengjing
     * @param       dto : openId不为空， 根据openId校验
     * @return      Result
     * @date        2019/5/6 16:59
     */
    @Override
    public Result checkPayPwd(IousRequestDTO dto) {
        /**解密RSA*/
        String payPwd = RSAEncrypt.getPassword(dto.getPayPassword(), FULI_RSA_USER);
        if(StringUtils.isNotBlank(dto.getId())){
            AppUser appUser = appUserDao.selectById(dto.getId());
            if(appUser != null && payPwd.equals(appUser.getPayPassword())){
                return Result.succeed(true);
            }
        } else if(StringUtils.isNotBlank(dto.getOpenId())){
            QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("id");
            queryWrapper.eq("pay_password", payPwd ).eq("openId",dto.getOpenId()).apply("1=1 LIMIT 1");
            AppUser appUser = appUserDao.selectOne(queryWrapper);
            if(appUser != null){
                return Result.succeed(true);
            }
        }
        return Result.failed(false);
    }
}

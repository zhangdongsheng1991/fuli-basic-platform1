package com.fuli.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.fuli.auth.common.model.BaseUser;
import com.fuli.auth.common.utils.Md5Utils;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.PageResult;
import com.fuli.user.commons.Result;
import com.fuli.user.configuration.service.ProductionAccessTokenService;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.dao.AppUserDao;
import com.fuli.user.dao.DataRecordUserMapper;
import com.fuli.user.dto.*;
import com.fuli.user.feign.BasicServerFeign;
import com.fuli.user.feign.UpushMessageFetFeign;
import com.fuli.user.model.AppUser;
import com.fuli.user.model.DataRecordUserDO;
import com.fuli.user.service.AppUserLoginAndRegisterService;
import com.fuli.user.service.RealNameInTaiwanService;
import com.fuli.user.utils.*;
import com.fuli.user.vo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Description: APP用户登录注册逻辑处理
 * @Author: FZ
 * @CreateDate: 2019/4/16 11:44
 * @Version: 1.0
 */
@Service
@Slf4j
public class AppUserLoginAndRegisterServiceImpl implements AppUserLoginAndRegisterService {

    @Resource
    private AppUserDao appUserDao;
    @Resource
    private DataRecordUserMapper dataRecordUserMapper;
    @Autowired
    private AppUserInfoServiceImpl appUserInfoServiceImpl;
    @Autowired
    private BasicServerFeign basicServerFeign;
    @Autowired
    private UpushMessageFetFeign upushMessageFetFeign;
    @Autowired
    private RealNameInTaiwanService realNameInTaiwanService;
    @Autowired
    private RedisService redisService;
    @Autowired(required = false)
    private ProductionAccessTokenService productionAccessTokenService;
    @Value("${FULI_RSA_USER}")
    private String FU_LI_RSA_USER;

    /**
     * 根据用户名获取UserDetails对象 （授权）
     * @param username :用户名
     * @return AppUserVo
     * @throws
     * @author FZ
     * @date 2019/4/15 9:00
     */
    @Override
    public AppUserVo findAppUserByUser(String username) {
        log.info("查询用户：",username);
        AppUserVo appUser = appUserDao.findUserOauthByPhone(username);
        if(appUser == null){
            log.info("用户查询失败");
            return appUser;
        }
        return packagingUserInfo(appUser);
    }

    /**
     * 中台获取token
     * @author      fengjing
     * @param       id：用户id
     * @param       companyId：企业id
     * @return      AppUserVo
     * @date        2019/6/17 16:05
     */
    @Override
    public AppUserVo getUserNews(String id , String companyId) {
        log.info("中台获取token查询参数id:{},companyId：{}",id,companyId);
        if (StringUtils.isNotBlank(id)){
            return appUserDao.findCompanyAndUserById(id,companyId);
        }
        return null;
    }

    /**
     * 封装用户返回信息， 添加企业员工等信息
     * @author      WFZ
     * @param       appUser
     * @return      Result
     * @date        2019/8/6 12:51
     */
    public AppUserVo packagingUserInfo(AppUserVo appUser){
        /** 查询用户所属企业列表*/
        List<CompanyByUserIdVO> companyList = appUserDao.listCompanyByUserIdAndOpenSaAs(Long.valueOf(appUser.getId()),null);
        if (CollectionUtils.isNotEmpty(companyList)){
            CompanyByUserIdVO company = new CompanyByUserIdVO();
            for(CompanyByUserIdVO vo : companyList){
                if(vo.getIsShow().equals(String.valueOf(1))){
                    company = vo;
                    break;
                }
            }
            /** 如果没有选择默认企业， 取第一个*/
            if (PublicUtil.isEmpty(company.getCompanyId())){
                company = companyList.get(0);
            }
            appUser.setCompanyId(company.getCompanyId());
            appUser.setEmployeeId(company.getEmployeeId());
            appUser.setCompanyCreditCode(company.getCompanyCreditCode());
            appUser.setEmployeeStatus(company.getEmployeeStatus());
            log.info("APP用户登录时企业信息CompanyByUserIdVO:{}",company);
        }
        return appUser;
    }


    /**
     * SaaS门户登录时获取用户信息
     *
     * @param phone :手机号
     * @return Result
     * @author WFZ
     * @date 2019/8/5 16:05
     */
    @Override
    public AppUserVo saAsLoginByPhone(String phone) {
        AppUserVo appUser = appUserDao.findUserOauthByPhone(phone);
        if (appUser != null){
            List<CompanyByUserIdVO> companyList = appUserDao.listCompanyByUserIdAndOpenSaAs(Long.valueOf(appUser.getId()),1);
            if (CollectionUtils.isNotEmpty(companyList)){
                CompanyByUserIdVO company = new CompanyByUserIdVO();
                /** 判断是否有添加默认展示企业*/
                for (CompanyByUserIdVO vo : companyList){
                    if (PublicUtil.isNotNull(vo.getDId())){
                        company = vo;
                    }
                }
                /** 如果没有添加默认企业或者添加的默认企业没开通SaaS门户权限，那默认取第一个*/
                if (PublicUtil.isEmpty(company.getCompanyId())){
                    company = companyList.get(0);
                }
                appUser.setRealName(company.getName());
                appUser.setCompanyId(company.getCompanyId());
                appUser.setEmployeeId(company.getEmployeeId());
                appUser.setEmployeeStatus(company.getEmployeeStatus());
                appUser.setCompanyCreditCode(company.getCompanyCreditCode());
                log.info("SaaS门户登录时企业信息CompanyByUserIdVO:{}",company);
            }
        }
        return appUser;
    }


    /**
     * SaaS门户登录
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/8/5 16:05
     */
    @Override
    public Result saAsLogin(UserLoginDTO dto) {
        log.info("SaaS门户登录，帐号phone:{}",dto.getPhone());
        /** 校验验证码是否有效 */
        Result resultImageCode = basicServerFeign.verificationImageCode(dto.getImageCode());
        if(! PublicUtil.isResultSuccess(resultImageCode)){
            return resultImageCode;
        }

        /** SaaS登录 phone即是手机号又是账号名 */
        AppUserVo appUser = saAsLoginByPhone(dto.getPhone());
        if (PublicUtil.isEmpty(appUser)){
            return Result.failed(CodeEnum.ACCOUNT_NO_EXISTS);
        }
        /** 密码解密*/
        String password = RSAEncrypt.getPassword(dto.getPassword(),FU_LI_RSA_USER);
        /** 对比密码， 如果是中台用户需要去middle-server 校验密码*/
        if (! CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom())){
            /*Result result = middleServerFeign.thirdLogin(appUser.getPhone(), password);*/
            Result result = realNameInTaiwanService.thirdLogin(appUser.getPhone(), password);
            if(! PublicUtil.isResultSuccess(result)){
                log.info("中台用户校验结果==="+result);
                return result;
            }
        }else{
            if (! password.equals(appUser.getPassword())){
                return Result.failed(CodeEnum.PASSWORD_CODE_ERROR);
            }
            /** 如果密码为初始 111111 或者 888888 则返回修改密码*/
            if ((CommonConstant.USER_INIT_PASSWORD).equals(password) || (CommonConstant.ADMIN_INIT_PASSWORD).equals(password)){
                return Result.failed(CodeEnum.USER_INIT_PASSWORD_UPDATE);
            }
        }
        return getAccessToken(appUser,2,null,null);
    }

    /**
     * APP用户注册
     *
     * @param dto
     * @return Result
     * @author FZ
     * @date 2019/4/15 19:03
     */
    @Override
    public Result appUserRegister(UserRegisterDTO dto) {
        /** 1.数据校验 */
        if(! RegexUtil.find(RegexUtil.PHONE,dto.getPhone())){
            return Result.failed(CodeEnum.PHONE_FORMAT_ERROR);
        }
        UserLoginVO vo = new UserLoginVO();
        vo.setPhone(dto.getPhone());
        vo.setVerifyCode(dto.getVerifyCode());
        Result result  = smsVerifyCode(vo, CommonConstant.SMS_SEND_SOURCE_REGISTER);
        if (! PublicUtil.isResultSuccess(result)){
            return result;
        }
        try{
            /** 防止并发,防止死锁加一个时间10秒*/
            String key = "sync_data_user_register: |" + dto.getPhone();
            boolean successFlag = RedisLockUtil.tryLock(key,1,10);
            if (!successFlag){
                return Result.failed(CodeEnum.USER_NOT_EMPTY);
            }
        }catch (Exception e){log.error("注册分布式锁",e);}
        /** 2.保证用户唯一，先验证用户是否已存在 */
        AppUser user = appUserDao.findUserByPhone(dto.getPhone());
        if (user != null) {
            return Result.failed(CodeEnum.USER_NOT_EMPTY);
        }

        /** 密码解密*/
        String password = RSAEncrypt.getPassword(dto.getPassword().trim() , FU_LI_RSA_USER);
        if (StringUtils.isBlank(password)){
            return Result.failed(CodeEnum.PASSWORD_NOT_NULL.getCode(),"非法密码");
        }

        /** 3.补充数据并插入 */
        Result rst = insertAppUserAndMsg(dto.getPhone(),password);
        if (rst.getCode() != CommonConstant.SUCCESS){
            return rst;
        }
        /** 5. 默认登陆，获取token 密码模式 */
        vo.setDeviceToken(dto.getDeviceToken());
        Result accessToken = getAccessToken((AppUser) rst.getData(), vo);
        if (PublicUtil.isResultSuccess(accessToken)){
            accessToken.setMsg("注册成功");
        }
        return accessToken;

    }

    /**
     * APP用户登录
     *
     * @param userLoginVo :用户登录VO
     * @return Result
     * @author FZ
     * @date 2019/4/15 9:35
     */
    @Override
    public Result appUserLogin(UserLoginVO userLoginVo) {
        /**
         * 判断登录模式 (如果是用户名密码模式， 密码需先解密)
         *  1-用户名密码模式 ， 2-用户名验证码
         */
        if (CommonConstant.LOGIN_PASSWORD == userLoginVo.getType()) {
            /**
             * 根据手机号获取用户相信信息 , 如果无密码说明是第一次登录并且是中台用户，需到中台验证
             */
            AppUser user = appUserDao.findAppUserByPhone(userLoginVo.getPhone());
            if (user == null) {
                return Result.failed(CodeEnum.USER_NOT_FOUND);
            }
            return passwordModeLogin(userLoginVo, user , 1);
        } else if (CommonConstant.LOGIN_CODE == userLoginVo.getType()) {
            /**
             * 验证码登录如果用户不存在则插入一条数据
             */
            return verifyCodeModeLogin(userLoginVo);

        } else {
            log.info("[用户登录]登录模式有误");
            return Result.failed(CodeEnum.LOGIN_TYPE_ERROR);
        }
    }


    /**
     * 根据openID获取主键唯一ID
     * @author      fengjing
     * @date        2019/5/11 14:48
     */
    @Override
    public Result obtainPrimaryKey(String openId) {
        if(StringUtils.isEmpty(openId)){
            return Result.failed("openId不能为空");
        }
        return Result.succeed(appUserDao.findUserByOpenId(openId));
    }

    /**
     * @Description:(分页查询用户手机号)
     * @author      fengjing
     * @date        2019/5/14 19:13
     */
    @Override
    public PageResult pageUserPhone(Map<String, Object> map) {
        int pageSize = (int) map.get("pageSize");
        int pageNum = (int) map.get("pageNum");
        //这是整个过程中最核心的一条语句
        Page<Object> page = PageHelper.startPage(pageNum, pageSize, true);
        List<AppUser> allUser = appUserDao.getAllUser();
        return PageResult.builder().records(allUser).totalPage(page.getPages()).total(page.getTotal()).currentPage(page.getPageNum()).pageSize(page.getPageSize()).build();
    }

    /**
     * 中台用户同步接口
     * @author      fengjing
     * @param 	   appUserDTO
     * @return      Result
     * @date        2019/8/12 15:09
     */
    @Override
    public Result platformUserSynchronized(AppUserDTO appUserDTO) {
        log.info("接收到中台同步过来的用户数据:{}",appUserDTO.getOpenId());
        /** 数据转化*/
        AppUser user = new AppUser();
        user.setOpenid(appUserDTO.getOpenId());
        user.setPhone(appUserDTO.getPhoneNumber());
        user.setRealName(appUserDTO.getUsername());
        user.setCertificateCard(appUserDTO.getCertificateCard());
        user.setCertificateType(appUserDTO.getCertificateType()==null?1:appUserDTO.getCertificateType());
        user.setGender(appUserDTO.getGender());
        user.setStatus(appUserDTO.getStatus());
        user.setEmail(appUserDTO.getEmail());
        user.setIsIdentification(1);
        /** 先根据openId查询用户是否存在*/
        AppUser appUser = appUserDao.findUserByOpenId(appUserDTO.getOpenId());
        if(appUser != null){
            user.setId(appUser.getId());
            /** 存在就更新*/
            if (appUser.getIsIdentification().intValue() != 1 ){
                user.setUpdateTime(new Date());
                /** 如果原数据没有系统帐号就添加*/
                if (StringUtils.isBlank(appUser.getUsername())){
                    user.setUsername(changeUsername(appUserDTO.getUsername()));
                }
                int i = appUserDao.updateById(user);
                if(i != 1){
                    return Result.failed();
                }
            }
        }else{
            /** 判断手机号是否存在，存在直接返回失败*/
            AppUser byPhone = appUserDao.findUserByPhone(appUserDTO.getPhoneNumber());
            if(byPhone != null ){
                /** 如果未实名返回错误， 如果已实名没有openid则更新*/
                if (byPhone.getIsIdentification().intValue() == 1 && StringUtils.isBlank(byPhone.getOpenid())){
                    byPhone.setOpenid(appUserDTO.getOpenId());
                    appUserDao.updateById(byPhone);

                    user.setId(byPhone.getId());
                }else {
                    return Result.failed();
                }
            }else {
                user.setUsername(changeUsername(appUserDTO.getUsername()));
                user.setUserFrom(appUserDTO.getUserFrom());
                user.setCreateTime(new Date());
                int i = appUserDao.insert(user);
                if(i != 1){
                    return Result.failed();
                }
            }
        }

        /** 中台用户 保存/修改 成功后返回自增id给中台*/
        Map<String,String> retMap =new HashMap<>(4);
        retMap.put("openId",appUserDTO.getOpenId());
        retMap.put("thirdUserId",user.getId().toString());
        log.info("中台同步后的自增id："+user.getId());
        return Result.succeed(retMap);
    }

    /**
     * 真实姓名转化成拼音， 作为帐号
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/8/14 15:44
     */
    public String changeUsername(String username){
        /** 转成拼音*/
        String userAccount = SpellHelperUtil.toPinyin(username);
        if (StringUtils.isNotBlank(userAccount)){
            /** 查询是否存在*/
            Integer count = appUserDao.getCountByLikeUserName(userAccount);
            /** 将用户名转为拼音并且获取当前用户的系统账号是多少 */
            if(count != null && count.intValue() > 0){
                userAccount = userAccount + ( count + 1);
            }

        }
        return userAccount;
    }


    /**
     * 订阅中台用户修改
     * @author      fengjing
     * @param       dto:
     * @return      Result
     * @date        2019/5/16 20:02
     */
    @Override
    public Result platformUpdate(PlatformUpdateDTO dto) {
        log.info("中台修改用户数据参数：",dto);
        if (PublicUtil.isNotNull(dto.getThirdUserId())){
            AppUser appUser = new AppUser();
            appUser.setId(dto.getThirdUserId());
            appUser.setPhone(dto.getPhoneNumber());
            appUser.setEmail(dto.getEmail());
            appUser.setStatus(dto.getStatus());
            int i = appUserDao.updateById(appUser);
            if(i != 1){
                return Result.failed();
            }
            return Result.succeed();
        }
        return Result.failed("用户id不能为空");
    }



    /**
     * @Description:(设置手势密码、修改手势密码)
     * @author      fengjing
     * @date        2019/6/3 17:46
     */
    @Override
    public Result gesturePassword(ChangeGesturePwdDTO dto , TokenUserVO userVO) {
        AppUser users = new AppUser();
        users.setId(userVO.getId());
        users.setGesturePassword(dto.getGesturePassword());
        users.setGestureSwitch(dto.getGestureSwitch());
        users.setIsDisplayTrajectory(dto.getIsDisplayTrajectory());
        int i = appUserDao.updateById(users);
        if (i == 1){
            return Result.succeed();
        }else {
            return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
        }

    }

    /**
     * 用户名验证码模式登录
     *
     * @param userLoginVo
     * @return Result
     * @author FZ
     * @date 2019/4/15 14:10
     */
    public Result verifyCodeModeLogin(UserLoginVO userLoginVo) {
        /** 验证码校验*/
        if(StringUtils.isEmpty(userLoginVo.getVerifyCode())){
            return Result.failed(CodeEnum.VERIFY_CODE_EMPTY);
        }
        Result r  = smsVerifyCode(userLoginVo,CommonConstant.SMS_SEND_SOURCE_LOGIN);
        if (r.getCode() != CommonConstant.SUCCESS){
            return r;
        }
        // TODO 验证码登录如果用户不存在则插入一条数据
        AppUser user = appUserDao.findUserByPhone(userLoginVo.getPhone());
        if (user == null){
            /** 插入一条新数据*/
            Result result = insertAppUserAndMsg(userLoginVo.getPhone(),null);
            if (result.getCode() == CommonConstant.SUCCESS){
                user = (AppUser)result.getData();
            }else{
                return result;
            }
        }
        /** 获取token*/
        return getAccessToken(user , userLoginVo);
    }


    /**
     * 用户名密码模式登录
     *
     * @param userLoginVo
     * @param user AppUser
     * @param type : 1- 需要获取token ， 2- 只校验密码
     * @return Result
     * @author FZ
     * @date 2019/4/15 14:10
     */
    public Result passwordModeLogin(UserLoginVO userLoginVo, AppUser user,int type) {
        String pwd = userLoginVo.getPassword();
        /** 数据校验*/
        if (StringUtils.isEmpty(pwd)) {
            return Result.failed(CodeEnum.PASSWORD_NOT_NULL);
        }
        /** 记录密码错误的redis key*/
        String redisKey = CommonConstant.APP_USER_PASSWORD + user.getId();
        /** 如果大于第5次才输入成功，也不给登录*/
        if (redisService.exists(redisKey)) {
            if (((int)redisService.get(redisKey)) >= CommonConstant.PASSWORD_ERROR_COUNT) {
                /** 获取过期时间*/
                Long expireDateTime = redisService.getExpireDateTime(redisKey, TimeUnit.MINUTES);
                return Result.failedWith(null, CodeEnum.PASSWORD_CODE_ERROR.getCode(), "登录密码输入超过5次，账户已被锁定，请" + expireDateTime + "分钟后重试或找回密码");
            }
        }
        /** 密码解密*/
        String password = RSAEncrypt.getPassword(pwd,FU_LI_RSA_USER);
        /** userFrom != FLJR 表示中台用户 */
        if (! CommonConstant.FU_LI_LABEL.equals(user.getUserFrom())){
            /** 调用middle-server的接口 去中台校验 调取中台接口如果返回为空表示用户名或者密码有误*/
            Result result = realNameInTaiwanService.thirdLogin(user.getPhone(), password);
            if(! PublicUtil.isResultSuccess(result)){
                log.info("中台用户校验结果==="+result);
                return appLoginPassWord(user);
            }
        }else{
            /** 校验数据库密码 */
            if (StringUtils.isEmpty(user.getPassword()) || !user.getPassword().equals(password)) {
                /**判断用户是否连续输入五次错误*/
                return appLoginPassWord(user);
            }
        }
        /** 登录成功清空redis*/
        redisService.remove(redisKey);
        if (type != 2){
            /** 获取token*/
            return  getAccessToken(user ,userLoginVo);
        }
        return Result.succeed();
    }

    /**
     * 登录密码输错次数记录 -- 5次就锁定帐号
     * @author      WFZ
     * @param       user : 用户数据
     * @return      Result
     * @date        2019/8/9 15:17
     */
    public Result appLoginPassWord(AppUser user) {
        Result pwdResult = Result.failed(CodeEnum.PASSWORD_CODE_ERROR);
        /** 记录密码错误的redis key*/
        String redisKey = CommonConstant.APP_USER_PASSWORD + user.getId();
        /** 最高密码错误次数*/
        int totalCount = CommonConstant.PASSWORD_ERROR_COUNT;
        /** Redis为空表示第一次数错*/
        if (! redisService.exists(redisKey)) {
            redisService.set(redisKey, 1);
            pwdResult.setMsg("登录密码不正确，您还可以输入4次");
        } else {
            int count = (int) redisService.get(redisKey);
            /** 连续输错第5次密码的时候帐号锁定*/
            if ( count >= (CommonConstant.PASSWORD_ERROR_COUNT-1) ) {
                //设置key过期
                redisService.set(redisKey, 5 , 3600L);
                pwdResult.setMsg("登录密码输入超过5次,账户已被锁定,请60分钟后重试或找回密码");
            } else {
                redisService.set(redisKey, count + 1);
                pwdResult.setMsg("登录密码不正确，您还可以输入" + (totalCount - (count + 1)) + "次");
            }
        }
        return pwdResult;
    }

    /**
     * 封装验证码校验
     * @author      FZ
     * @param 	    userLoginVo
     * @param 	    smsCodeKey
     * @return      Result
     * @date        2019/4/22 15:01
     */
    public Result smsVerifyCode(UserLoginVO userLoginVo, String smsCodeKey) {
        try {
            SmsVerifyCodeDTO dto = new SmsVerifyCodeDTO();
            dto.setCode(userLoginVo.getVerifyCode());
            dto.setMobile(userLoginVo.getPhone());
            dto.setSmsSendSource(smsCodeKey);
            return basicServerFeign.smsVerifyCode(dto);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.failed("basic-center服务出错");
    }


    /**
     * 获取token公共方法
     * @author      WFZ
     * @param 	    appUserVo ： token自定义信息
     * @param 	    type ： 1- app , 2-SaaS门户 , 3- 中台获取
     * @return      Result
     * @date        2019/9/11 16:39
     */
    public Result getAccessToken(AppUserVo appUserVo, int type, String clientId, String clientSecret){
        try {
            BaseUser jwtDTO = new BaseUser();
            jwtDTO.setUserId(appUserVo.getId());
            jwtDTO.setPhone(appUserVo.getPhone());
            jwtDTO.setRealName(appUserVo.getRealName());
            jwtDTO.setUserAccount(appUserVo.getUsername());
            jwtDTO.setCurrentCompanyId(appUserVo.getCompanyId());
            jwtDTO.setEmployeeId(appUserVo.getEmployeeId());
            jwtDTO.setCompanyCreditCode(appUserVo.getCompanyCreditCode());
            if (type == 1){
                jwtDTO.setClientId(CommonConstant.CLIENT_ID);
                return productionAccessTokenService.createAccessToken(CommonConstant.CLIENT_ID, CommonConstant.CLIENT_SECRET, CommonConstant.CLIENT_GRANT_TYPE, jwtDTO,1);
            }else if (type == 2){
                jwtDTO.setClientId(CommonConstant.CLIENT_ID_PORTAL);
                return productionAccessTokenService.createAccessToken(CommonConstant.CLIENT_ID_PORTAL, CommonConstant.CLIENT_SECRET_PORTAL, CommonConstant.CLIENT_GRANT_TYPE, jwtDTO,2);
            }else if (type == 3){
                jwtDTO.setClientId(clientId);
                return productionAccessTokenService.createAccessToken(clientId, clientSecret, CommonConstant.CLIENT_GRANT_TYPE, jwtDTO,3);
            }
        }catch (Exception e){
            log.error("获取token出错",e);
        }
        return Result.failed(CodeEnum.GET_ACCESS_TOKEN_FAIL);
    }

    /**
     * 封装获取accessToken方法，将保存到redis等操作一起放在此处 （手机号，验证码模式）
     * @author      WFZ
     * @param 	    user ： 用户信息
     * @return      userLoginVo: 登录注册请求对象
     * @exception
     * @date        2019/5/21 20:56
     */
    private Result getAccessToken(AppUser user, UserLoginVO userLoginVo){
        /**
         * 首先判断是否有帐号登录
         */
        repeatLoginPushNotification(user.getId(), userLoginVo.getDeviceToken());

        AppUserVo appUserVo = new AppUserVo();
        appUserVo.setId(user.getId().toString());
        appUserVo.setRealName(user.getRealName());
        appUserVo.setUsername(user.getUsername());
        appUserVo.setPhone(user.getPhone());
        appUserVo = packagingUserInfo(appUserVo);

        return getAccessToken(appUserVo,1,null,null);
    }


    /**
     * 封装，同一用户多个设备登录，前一个设备退出并发送通知
     * @author      WFZ
     * @param 	    id ： 用户id
     * @param 	    deviceNumber ： 设备号
     * @return       void
     * @date        2019/5/21 20:39
     */
    private void repeatLoginPushNotification(Long id , String deviceNumber){
        /**
         * 先查看redis里面是否存在，不存在表示第一次登录需要保存到redis中
         */
        if(redisService.exists(CommonConstant.FULI_DEVICE_NUM+id)){
            Object object = redisService.get(CommonConstant.FULI_DEVICE_NUM + id);
            if(null != deviceNumber && ! deviceNumber.equals(""+object)){
                //如果设备号不一致，则通过友盟退出上一个设备并且删除上一个设备保存新设备号
                PushVO pushVo = new PushVO();
                pushVo.setTitle("下线通知");
                pushVo.setText("账户在其它设备登录强制下线通知");
                pushVo.setOperatorId(id);
                pushVo.setDeviceToken(""+object);
                pushVo.setLogout("logout");
                Result result = upushMessageFetFeign.sendMsgUniCast(pushVo);
                log.info("友盟推送===",result);
            }
        }
        /** 将设备号保存到redis*/
        redisService.set(CommonConstant.FULI_DEVICE_NUM + id, deviceNumber);
    }


    /**
     * 新注册插入用户数据，并插入系统消息
     * @param phone ：手机号
     * @param password ： 密码
     */
    private Result insertAppUserAndMsg(String phone, String password){
        String key = "sync_data_user_register: |" + phone;
        try{
            AppUser users = new AppUser();
            users.setPassword(password);
            users.setPhone(phone.trim());
            users.setCreateTime(new Date());
            users.setUpdateTime(new Date());
            int insert = appUserDao.insert(users);

            /*// 用户消息设置  TODO 2019-11-28开始不插入初始化消息
            UserMsgDTO userMsgDTO = new UserMsgDTO();
            userMsgDTO.setUserId(users.getId());
            userMsgDTO.setEnableFlag(1);
            appUserMsgSetFeign.userMessageSettings(userMsgDTO);*/

            /** 插入同步中间表*/
            DataRecordUserDO data = new DataRecordUserDO();
            data.setUserId(users.getId());
            data.setPhone(phone);
            data.setPassword(password);
            data.setIsCertificated(0);
            data.setStatus(1);
            data.setUserFrom(CommonConstant.FU_LI_LABEL);
            data.setCreateTime(new Date());
            data.setType(1);
            dataRecordUserMapper.insert(data);
            return Result.succeed(users);
        }catch (Exception e){
            log.error("用户注册失败",e.getMessage());
        }finally {
            /** 释放锁*/
            RedisLockUtil.unlock(key);
        }
        return Result.failed(CodeEnum.ERROR);
    }


    @Override
    public Result resetPassword(ResetPasswordDTO dto) {
        AppUser appUser = appUserDao.selectById(dto.getUserId());
        if (appUser == null){
            return Result.failed("数据有误，请联系管理员");
        }
        String password = "";
        if (StringUtils.isNotBlank(dto.getPassword())){
            /** 密码解密*/
            password = RSAEncrypt.getPassword(dto.getPassword() , FU_LI_RSA_USER);
            if (StringUtils.isBlank(password)){
                log.info("密码非法，无法解析");
                return Result.failed("密码非法");
            }
        }else {
            password = Md5Utils.encode("888888");
        }
        appUser.setPassword(password);
        appUser.setUpdateTime(new Date());

        /** 修改密码后同步到中台*/
        PlatformPasswordDTO dtos = new PlatformPasswordDTO();
        dtos.setThirdUserId(appUser.getId().toString());
        dtos.setLoginName(appUser.getPhone());
        dtos.setPassword(password);
        dtos.setUserFrom(appUser.getUserFrom());

        /** 全部修改密码都要同步中台*/
        Result result = realNameInTaiwanService.syncPlatformPwd(dtos);
        log.info("重置密码同步中台返回结果：{}",result);
        /** 如果是中台的用户要传原密码*/
        if(CommonConstant.FU_LI_LABEL.equals(appUser.getUserFrom())){
            int i = appUserDao.updateById(appUser);
            if (i < 1){
                return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
            }
            return Result.succeed();
        }
        return result;
    }

    @Override
    public Map<String, Long> getAccounts(Set<String> accounts) {
        QueryWrapper<AppUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","username");
        queryWrapper.eq("status",1);
        queryWrapper.in("username", accounts);

        List<AppUser> appUsers = appUserDao.selectList(queryWrapper);
        Map<String,Long> users = new HashMap<>(16);
        if (CollectionUtils.isNotEmpty(appUsers)){
            appUsers.forEach( user -> {
                users.put(user.getUsername(), user.getId());
            });
        }
        return users;
    }

}

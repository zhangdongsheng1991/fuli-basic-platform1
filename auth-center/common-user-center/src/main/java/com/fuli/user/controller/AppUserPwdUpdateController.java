package com.fuli.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.user.commons.CodeEnum;
import com.fuli.user.commons.Result;
import com.fuli.user.commons.base.BaseController;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.dao.EmployeeMapper;
import com.fuli.user.dto.*;
import com.fuli.user.feign.BasicServerFeign;
import com.fuli.user.model.AppUser;
import com.fuli.user.model.EmployeeDO;
import com.fuli.user.service.AppUserPwdUpdateService;
import com.fuli.user.vo.CompanyUserVO;
import com.fuli.user.vo.TokenUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.DynamicParameter;
import io.swagger.annotations.DynamicParameters;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description:    APP用户密码修改模块
 * @Author:         xq
 * @CreateDate:     2019/4/16 16:24
 * @Version:        1.0
 */
@Slf4j
@RestController
@RequestMapping("/appUser")
@Api(tags = "用户密码管理模块")
public class AppUserPwdUpdateController extends BaseController {

    @Autowired
    AppUserPwdUpdateService appUserPwdUpdateService;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    BasicServerFeign basicServerFeign;

    /**
     * 修改用户密码  2019-08-09 接口修改(优化) WFZ
     * @author      xq
     * @param       request 请求参数封装
     * @return      Result
     * @date        2019/4/16
     */
    @PostMapping("/resetPassword")
    @ApiOperation(value = "APP用户密码修改")
    public Result updatePasswordByPhone(@Validated @RequestBody AppUserUpdatePasswordRequest request){
        log.info("APP用户修改密码参数：{}", request.toString());
        if(StringUtils.isEmpty(request.getUsedPwd())){
            return Result.failed(CodeEnum.PLATFORM_OLDNEW_PWD_ATYPISM);
        }
        TokenUserVO userInfo = getAppUserInfo();
        if(StringUtils.isEmpty(request.getPhone())){
            request.setPhone(userInfo.getPhone());
        }
        request.setUserId(userInfo.getId());

        return appUserPwdUpdateService.updatePasswordByPhone(request,2);
    }

    /**
     * 忘记密码
     * @author      xq
     * @param       request 请求参数封装
     * @return      Result
     * @date        2019/4/16
     */
    @PostMapping("/forgetPassword")
    @ApiOperation(value = "APP用户忘记密码")
    public Result forgetPasswordByPhone(@Validated @RequestBody AppUserUpdatePasswordRequest request){
        log.info("用户忘记密码：{}",request.toString());
        if (StringUtils.isBlank(request.getPhone())){
            return Result.failed("手机号码不能为空");
        }
        return appUserPwdUpdateService.updatePasswordByPhone(request,3);
    }

    /**
     * 修改用户支付密码（包括、设置支付密码，忘记支付密码，修改支付密码） 2019-08-09 接口修改(优化) WFZ
     * @author      xq
     * @param       appUserUpdatePayPwdRequest 请求参数封装
     * @date        2019/4/16
     */
    @PostMapping("/resetPayPassword")
    @ApiOperation(value = "修改用户支付密码（包括、设置支付密码，忘记支付密码，修改支付密码")
    public Result updatePayPasswordByPhone(@Validated @RequestBody AppUserUpdatePayPwdRequest appUserUpdatePayPwdRequest){
        log.info("修改用户支付密码:{}",appUserUpdatePayPwdRequest.toString());
        return appUserPwdUpdateService.updatePayPasswordByPhone(appUserUpdatePayPwdRequest,getAppUserInfo());
    }

    /**
     * 登录密码校验  2019-08-09 接口修改(优化) WFZ
     * @author      fengjing
     * @param       map -  password : 密码
     * @date        2019/5/6 16:52
     */
    @ApiOperation(value = "登录密码校验，根据id和password")
    @PostMapping(value = "/logonPwdValidity")
    public Result logonPwdValidity(@RequestBody Map<String,Object> map){
        return appUserPwdUpdateService.logonPwdValidity(map,getAppUserInfo());
    }


    /**
     * 发送验证码
     * @author      xq
     * @param       smsSendDto  手机验证码发送封装
     * @date        2019/4/16
     */
    @PostMapping(value = "/sendSmsCode")
    @ApiOperation(value = "验证码发送")
    public Result sendSms(@RequestBody SmsSendDTO smsSendDto){
        log.info("发送短信验证码时，入参为{}",smsSendDto);
        String loginUserPhone = getLoginUserPhone();
        return appUserPwdUpdateService.sendSms(smsSendDto,loginUserPhone);
    }

    /**
     * 验证码校验
     * @author      xq
     * @param       smsVerifyCodeDto 验证码校验请求
     * @date        2019/4/16
     */
    @PostMapping("/smsValidateCode")
    @ApiOperation(value = "验证码校验")
    public Result sendSmsValidate(@RequestBody SmsVerifyCodeDTO smsVerifyCodeDto){
        if (StringUtils.isEmpty(smsVerifyCodeDto.getMobile())) {
            return Result.failed(CodeEnum.USER_NOT_MOBLIE_EMPTY);
        }
        if (! CommonConstant.SMS_SEND_SOURCE_LOGIN.equals(smsVerifyCodeDto.getSmsSendSource()) || ! CommonConstant.SMS_SEND_SOURCE_REGISTER.equals(smsVerifyCodeDto.getSmsSendSource()) ){
            AppUser user = appUserPwdUpdateService.findAppUserByPhone(smsVerifyCodeDto.getMobile());
            if(user == null){
                return Result.failed(CodeEnum.USER_NOT_FOUND_ERROR);
            }
        }
        return basicServerFeign.smsVerifyCode(smsVerifyCodeDto);
    }

    /**
     * 中台系统修改本系统用户密码时同步
     * @author      fengjing
     * @date        2019/6/6 10:39
     */
    @PostMapping("/sysnPassword")
    @ApiOperation(value = "中台系统修改本系统用户密码时同步")
    public Result syncPassword(@RequestBody Map<String,Object> map){
        return appUserPwdUpdateService.syncPassword(map);
    }


    @DynamicParameters(name = "Map",properties = {
            @DynamicParameter(name = "phone",value = "手机号码",required = true),
            @DynamicParameter(name = "userId",value = "用户id, 编辑员工时必传"),
            @DynamicParameter(name = "type",value = "1-找回密码校验，2-新增/编辑员工校验",example = "1"),
    })
    @PostMapping("/phoneVerification")
    @ApiOperation(value = "验证手机号是否存在")
    public Result phoneVerification(@RequestBody Map dateMap){
        if (dateMap.get("phone") == null){
            return Result.failed("手机号不能为空");
        }
        AppUser user = appUserPwdUpdateService.findAppUserByPhone(dateMap.get("phone")+"");
        if ((dateMap.get("type")+"").equals("2") ){
            if (dateMap.get("userId") != null){
                if (user != null &&  ! String.valueOf(user.getId()).equals(dateMap.get("userId")+"")){
                    return Result.failed("该手机号码已存在，请重新输入");
                }
            }else {
                if (user != null){
                    TokenUserVO tokenUserVO = getAppUserInfo();
                    /** 重新入职只要手机号不是本公司就可以*/
                    QueryWrapper<EmployeeDO> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("user_id", user.getId())
                            .eq("company_id", tokenUserVO.getCurrentCompanyId())
                            .eq("status", 1);
                    log.info(" 根据用户id{}查询本企业员工", user.getId(),"企业id{}",tokenUserVO.getCurrentCompanyId());
                    List<EmployeeDO> list = employeeMapper.selectList(queryWrapper);
                    if (! CollectionUtils.isEmpty(list)){
                        return Result.failed("该员工已存在，不可重复创建");
                    }
                }
            }
        }else {
            if(user == null){
                return Result.failed(CodeEnum.USER_NOT_FOUND_ERROR);
            }
        }
        return Result.succeed();
    }



    /**
     * 支付密码校验 --- 白条业务线
     * @author      fengjing
     * @date        2019/5/6 16:52
     */
    @ApiOperation(value = "支付密码校验，根据id和payPassword")
    @PostMapping(value = "/paymentPwd")
    public Result<Boolean> checkPayPwd(@RequestBody IousRequestDTO iousRequestDTO){
        return appUserPwdUpdateService.checkPayPwd(iousRequestDTO);
    }

    /**
     * 根据用户openid、payPassword校验密码  -- 白条业务线
     * @author      fengjing
     * @date        2019/5/28 10:17
     */
    @ApiOperation(value = "根据用户openid、payPassword校验密码")
    @PostMapping(value = "/companyUserVerifyPwd")
    public Result<Boolean> companyUserVerifyPwd(@RequestBody IousRequestDTO iousRequestDTO){
        return appUserPwdUpdateService.checkPayPwd(iousRequestDTO);
    }

    /**
     * 根据企业openid 用户openid返回用户、企业、员工信息 -- 白条业务线
     * @author      fengjing
     * @date        2019/5/28 14:21
     */
    @ApiOperation(value = "根据企业openid 用户openid返回用户、企业、员工信息")
    @PostMapping(value = "/companyUserData")
    public Result<CompanyUserVO> companyUserData(@RequestBody IousRequestDTO iousRequestDTO){
        return appUserPwdUpdateService.companyUserData(iousRequestDTO);
    }
}

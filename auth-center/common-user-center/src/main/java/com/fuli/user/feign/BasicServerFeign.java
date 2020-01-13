package com.fuli.user.feign;


import com.fuli.user.commons.Result;
import com.fuli.user.dto.SmsSendDTO;
import com.fuli.user.dto.SmsVerifyCodeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 密码修改用户发送手机验证码调用
 *
 * @author xq
 * @date 2019/04/18
 */
@FeignClient("basic-server")
public interface BasicServerFeign {
    /**
     * 发送手机验证码
     *
     * @param smsSendDto 手机验证码封装类
     * @return Result
     */
    @PostMapping(value = "/sms/sendSmsVerifyCode")
    Result sendSmsVerifyCode(@RequestBody SmsSendDTO smsSendDto);

    /**
     * 检验手机验证码
     *
     * @param smsVerifyCodeDto 手机验证码校验封装类
     * @return Result
     */
    @PostMapping(value = "/sms/verifyCode")
    Result smsVerifyCode(@RequestBody SmsVerifyCodeDTO smsVerifyCodeDto);

    /**
     * 图片验证码校验
     * @author      fengjing
     * @param 	    imageCode
     * @return      Result
     * @date        2019/7/9 19:55
     */
    @PostMapping(value = "/verifyCode/verification")
    Result verificationImageCode(@RequestParam("imageCode")String imageCode);
}

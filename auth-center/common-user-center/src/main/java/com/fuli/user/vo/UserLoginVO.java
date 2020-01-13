package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 用户登录VO
 * @Author: FZ
 * @CreateDate: 2019/4/15 9:03
 * @Version: 1.0
 */
@Data
public class UserLoginVO implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号/用户名不能为空")
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码（密码登录必填，验证码登录非必填）")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码（验证码登录必填，密码登录非必填）")
    private String verifyCode="888888";

    /**
     * 登录模式 1-用户名密码模式 ， 2-用户名验证码
     */
    @ApiModelProperty(value = "登录模式 1-用户名密码模式 ， 2-用户名验证码 ",required = true)
    private int type;


    @ApiModelProperty(value = "绑定手机时用")
    private String openId;

    /**
     * 设备号
     */
    @ApiModelProperty(value = "设备号")
    private String deviceToken;
}

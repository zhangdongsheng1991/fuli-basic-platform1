package com.fuli.user.dto;

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
public class UserRegisterDTO implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "短信验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    /**
     * 设备号
     */
    @ApiModelProperty(value = "设备号")
    private String deviceToken;
}

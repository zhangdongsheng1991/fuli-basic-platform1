package com.fuli.cloud.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description:    用户登录DTO
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 11:31
 * @Version:        1.0
*/
@Data
public class UserLoginDTO implements Serializable {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "手机号（系统账号）",required = true)
    @NotBlank(message = "用户名或者手机号不能为空")
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 图片验证码
     */
    @ApiModelProperty(value = "图片验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    /**
     * 图片验证码
     */
    @ApiModelProperty(value = "类别-区分登录系统；1 - SaaS运营，2- 渠道运营",required = true)
    @NotBlank(message = "登录类别不能为空")
    private String type;

}

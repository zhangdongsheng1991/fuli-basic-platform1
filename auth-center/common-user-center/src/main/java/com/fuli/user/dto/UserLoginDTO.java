package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description: 用户登录VO
 * @Author: WFZ
 * @CreateDate: 2019/4/15 9:03
 * @Version: 1.0
 */
@Data
public class UserLoginDTO implements Serializable {


    @ApiModelProperty(value = "手机号（系统账号）",required = true)
    @NotBlank(message = "手机号或用户名不能为空")
    private String phone;

    @ApiModelProperty(value = "用户密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "图片验证码",required = true)
    @NotBlank(message = "验证码不能为空")
    private String imageCode;

}

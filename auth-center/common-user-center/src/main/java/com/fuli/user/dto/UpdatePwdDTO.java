package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description:    密码修改请求封装
 * @Author:         xq
 * @CreateDate:     2019/4/16 16:24
 * @Version:        1.0
 */
@Data
public class UpdatePwdDTO implements Serializable {

    private static final long serialVersionUID = -1L;



    @ApiModelProperty(value = "登录帐号（手机号或帐号）",required = true)
    @NotBlank(message = "帐号不能为空")
    private String phone;

    @ApiModelProperty(value = "原密码",required = true)
    @NotBlank(message = "原密码不能为空")
    private String usedPwd;

    @ApiModelProperty(value = "新密码",required = true)
    @NotBlank(message = "登录密码不能为空")
    private String password;

    @ApiModelProperty(value = "输入密码",required = true)
    @NotBlank(message = "确认密码不能为空")
    private String retypePassword;
}

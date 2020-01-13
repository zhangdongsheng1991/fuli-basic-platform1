package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
public class AppUserUpdatePasswordRequest implements Serializable {
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "手机号",required = true)
    private String phone;
    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码",required = true)
    private String usedPwd;
    /**
     * 新本地登录密码
     */
    @ApiModelProperty(value = "登录密码",required = true)
    @NotBlank(message = "登录密码不能为空")
    private String password;
    /**
     * 再次输入密码
     */
    @ApiModelProperty(value = "再次输入密码",required = true)
    @NotBlank(message = "确认密码不能为空")
    private String retypePassword;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Long userId;
}

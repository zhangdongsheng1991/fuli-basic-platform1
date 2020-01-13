package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @Description: 密码修改请求封装
 * @Author: xq
 * @CreateDate: 2019/4/16 16:24
 * @Version: 1.0
 */
@Data
public class AppUserUpdatePayPwdRequest {
    /**
     * 用户手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号", required = true)
    private String phone;
    /**
     * 旧密码
     */
    @ApiModelProperty(name = "usedPwd",value = "旧密码",required = true)
    private String usedPwd;
    /**
     * 本地新支付密码
     */
    @ApiModelProperty(value = "本地新支付密码", required = true)
    @NotBlank(message = "支付密码不能为空")
    private String paypwd;
    /**
     * 再次输入的新支付密码
     */
    @ApiModelProperty(value = "再次输入的新支付密码")
    @NotBlank(message = "确认密码不能为空")
    private String retypePaypwd;

    /**
     * 区分设置支付密码：2、修改支付密码：1，忘记支付密码
     */
    @ApiModelProperty(name = "type",value = "区分,设置支付密码：2、修改支付密码：1，忘记支付密码",required = true)
    @Min(value = 1,message = "type不能为空")
    private int type;

}

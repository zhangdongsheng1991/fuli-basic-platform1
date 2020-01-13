package com.fuli.user.dto;

import com.fuli.user.commons.SmsSendSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangdongsheng
 * @date 2019/4/17
 */
@Data
public class SmsVerifyCodeDTO extends SmsSendDTO {
    /**
     * 验证码
     */
    @ApiModelProperty(name = "code", value = "验证码", required = true)
    private String code;
    /**
     * 发送类型 {@link SmsSendSourceEnum}
     */
    @ApiModelProperty(value = "发送类型(login(登录) , register(注册) , findPwd(查询密码) , binding(绑定) , forgetPwd (忘记密码), updatePwd (修改密码), updatePayPwd (修改支付密码), " +
            "openAccPDS(开通PDS) , openAccXinTuo(开通信托) ,updatePhone(修改手机号),payoff(发薪))", required = true)
    @NotBlank(message = "发送类型不能为空")
    private String smsSendSource;
}

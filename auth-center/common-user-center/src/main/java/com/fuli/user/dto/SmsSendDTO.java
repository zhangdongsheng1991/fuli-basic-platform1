package com.fuli.user.dto;

import com.fuli.user.commons.SmsSendSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 发送短信传入参数实体类
 * @author yhm
 * @date 2019/4/18
 */
@Data
public class SmsSendDTO implements Serializable {

    private static final long serialVersionUID = -5215638403185577433L;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "mobile", value = "手机号", required = true)
    private String mobile;

    /**
     * 发送类型 {@link SmsSendSourceEnum}
     */
    @ApiModelProperty(value = "发送类型(login(登录) , register(注册) , findPwd(查询密码) , binding(绑定) , forgetPwd (忘记密码), updatePwd (修改密码), updatePayPwd (修改支付密码), " +
            "openAccPDS(开通PDS) , openAccXinTuo(开通信托) ,updatePhone(修改手机号),payoff(发薪))", required = true)
    @NotBlank(message = "发送类型不能为空")
    private String smsSendSource;

    /**
     * 短信内容
     */
    @ApiModelProperty(name = "msg", value = "短信内容")
    private String msg;

    /**
     * 短信模板编号
     */
    @ApiModelProperty(name = "templateCode", value = "短信模板编号")
    private String templateCode;

    /**
     * 短信模板数组类型参数
     */
    @ApiModelProperty(name = "paramObj", value = "短信模板数组类型参数")
    private Object[] paramObj;
}

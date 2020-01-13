package com.fuli.cloud.dto.user;

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
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 发送类型
     */
    @ApiModelProperty(value = "发送类型(login , register , findPwd , binding , forgetPwd , updatePwd , updatePayPwd , openAccPDS , openAccXinTuo)", example = "forgetPwd", required = true)
    @NotBlank(message = "发送类型不能为空")
    private String smsSendSource;

}

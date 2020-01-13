package com.fuli.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description:    白条业务线强求类
 * @Author:         WFZ
 * @CreateDate:     2019/8/16 18:08
 * @Version:        1.0
*/
@Data
public class IousRequestDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value="APP用户id")
    private String id;

    @ApiModelProperty(value="支付密码")
    private String payPassword;

    @ApiModelProperty(value="用户在集团平台系统的唯一Id")
    private String openId;

    @ApiModelProperty(value="企业在集团平台系统的唯一Id")
    private String companyOpenId;

}

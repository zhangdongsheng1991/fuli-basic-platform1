package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Description:    中台获取token
 * @Author:         WFZ
 * @CreateDate:     2019/8/12 14:55
 * @Version:        1.0
*/
@Data
public class MiddleTokenDTO implements Serializable {

    @ApiModelProperty(value="用户id",required = true)
    @NotBlank(message = "用户id不能为空")
    private String appUserId;

    @ApiModelProperty(value="客户端id",required = true)
    @NotBlank(message = "客户端id不能为空")
    private String clientId;

    @ApiModelProperty(value="客户端密匙",required = true)
    @NotBlank(message = "客户端密匙不能为空")
    private String clientSecret;

    @ApiModelProperty(value="企业id")
    private String companyId;


}

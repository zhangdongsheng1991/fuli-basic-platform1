package com.fuli.cloud.dto.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 开通服务请求DTO
 * @author WFZ 2019-07-30
 */
@Data
@ApiModel("开通服务请求DTO")
public class OpenServiceDTO {

    @ApiModelProperty(value = "服务id",required = true)
    @NotBlank(message = "服务id不能为空")
    private String id;

    @ApiModelProperty(value = "企业id",required = true)
    @NotBlank(message = "企业id不能为空")
    private String companyId;

    @ApiModelProperty(value = "服务状态：1-开启，2-关闭 (SaaS门户只做记录，不做权限控制，业务线自己做权限控制)",required = true)
    @NotBlank(message = "服务状态不能为空")
    private String state;

    @ApiModelProperty(value = "是否开启项目管理 - 只有开通薪发放有此项（开通-true , 不开通-false）" , example = "false")
    private Boolean isOpenProject=false;

    public OpenServiceDTO() {
    }

}

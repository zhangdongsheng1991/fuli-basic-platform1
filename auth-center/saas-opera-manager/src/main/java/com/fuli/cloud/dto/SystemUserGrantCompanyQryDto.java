package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 运营用户关联企业权限查询类
 * @author yhm
 * @date 2019/12/4
 */
@Data
public class SystemUserGrantCompanyQryDto extends PageDTO implements Serializable {

    private static final long serialVersionUID = -21029264993545855L;

    @ApiModelProperty(value = "模块id")
    private Integer moduleId;

    @ApiModelProperty(value = "企业ID")
    private Long companyId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "管理人")
    private String name;

    @ApiModelProperty(value="手机号码")
    private String phoneNumber;


}

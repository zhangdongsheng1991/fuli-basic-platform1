package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 岗位入参类
 * @author yhm
 * @date 2019/06/25
 */
@Data
public class PositionDto extends PageDTO implements Serializable {

    private static final long serialVersionUID = -8161364501650409738L;

    @ApiModelProperty(value ="岗位ID")
    private Integer id;

    @ApiModelProperty(value ="部门ID")
    private Integer departmentId;

    @ApiModelProperty(value ="岗位名称")
    private String name;

    @ApiModelProperty(value ="岗位编码")
    private String code;

    @ApiModelProperty(value ="上级岗位ID")
    private Integer parentId;

    @ApiModelProperty(value = "状态 0：禁用；1：启用")
    private Byte status;
}
package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 部门入参类
 * @author yhm
 * @date 2019/06/25
 */
@Data
public class DepartmentDto extends PageDTO implements Serializable{

    private static final long serialVersionUID = -1989479967155123238L;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门编码")
    private String code;

    @ApiModelProperty(value = "状态 0：禁用；1：启用")
    private Byte status;
}
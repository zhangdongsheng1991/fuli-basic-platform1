package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author chenyi
 * @date 2019/7/30
 */
@Data
public class RoleIdNameVo implements Serializable {


    @ApiModelProperty(value = "角色id")
    private Integer id;

    @ApiModelProperty(value = "角色名称")
    private String name;

}

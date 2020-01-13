package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:    角色
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 19:30
 * @Version:        1.0
*/
@Data
public class RoleVO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("是否为企业超级管理 1 是 0 否 (超级管理员拥有企业所有权限)")
    private Byte administrators;



}
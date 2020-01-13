package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 */
@Data
public class LogoutSystemRoleDTO {

    /**
     * 角色表
     */
    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色下成员人数")
    private Integer roleUserCount;

    @ApiModelProperty("角色状态 0 未生效 1 已生效 2 已退回 3 已注销")
    private Integer status;
}

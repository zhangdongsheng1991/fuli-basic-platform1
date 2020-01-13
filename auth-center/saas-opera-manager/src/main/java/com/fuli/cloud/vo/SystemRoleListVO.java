package com.fuli.cloud.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 响应实体类
 * @author admin3
 */
@Data
public class SystemRoleListVO implements Serializable {

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

    @ApiModelProperty("是否超级管理员0-否 1-是")
    private Integer administrators;

    @ApiModelProperty("操作人用户账号")
    private String operationAccount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;
}

package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.constant.DateConstant;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
 *  角色表
 * @author WFZ 2019-07-29
 */
@Data
@TableName("system_role")
@FieldNameConstants
public class SystemRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色表
     */
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 角色名
     */
    private String name;

    /**
     * 角色状态 0 未生效 1 已生效 2 已退回 3 已注销
     */
    private Integer status;

    /**
     * 是否为企业超级管理 1 是 0 否 (超级管理员拥有企业所有权限)
     */
    private Integer administrators;

    /**
     * 审核退回信息
     */
    private String approvalReturnMsg;

    /**
     * 操作人用户账号
     */
    private String operationAccount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date updateTime;

    /**
     * 角色描述
     */
    private String description;

    public SystemRoleDO() {
    }

}
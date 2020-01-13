package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 *  用户角色中间表
 * @author WFZ 2019-07-29
 */
@Data
@TableName("system_role_user")
@FieldNameConstants
public class SystemRoleUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色用户关联表 - 角色id
     */
    private Integer roleId;

    /**
     * 用户id
     */
    private Integer userId;

    public SystemRoleUserDO() {
    }

    public SystemRoleUserDO(Integer roleId, Integer userId) {
        this.roleId = roleId;
        this.userId = userId;
    }
}
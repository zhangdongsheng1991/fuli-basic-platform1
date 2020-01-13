package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色首页模块关联表
 * @author xq
 */
@Data
@TableName("system_role_homepage_module")
public class SystemRoleHomepageModuleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色与首页模块中间表
     */
    private Integer roleId;

    /**
     * 首页模块id
     */
    private Integer homepageModuleId;

    public SystemRoleHomepageModuleDO() {
    }

    public SystemRoleHomepageModuleDO(Integer roleId, Integer homepageModuleId) {
        this.roleId = roleId;
        this.homepageModuleId = homepageModuleId;
    }
}
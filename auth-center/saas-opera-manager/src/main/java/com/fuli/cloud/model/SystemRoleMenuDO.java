package com.fuli.cloud.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 *  角色菜单关联表
 * @author xq 2019-07-29
 */
@Data
@TableName("system_role_menu")
public class SystemRoleMenuDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色菜单中间表 - 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;

    public SystemRoleMenuDO() {
    }

    public SystemRoleMenuDO(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}

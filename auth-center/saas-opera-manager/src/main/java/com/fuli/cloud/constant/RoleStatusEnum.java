package com.fuli.cloud.constant;

import io.swagger.models.auth.In;

/**
 * 角色状态枚举
 * @author pcg
 * @date 2019-6-21 14:22:27
 */
public enum RoleStatusEnum {
    /**
     * 角色状态(0：未生效 1:已生效 2:已退回 3:已注销)
     */
    NO_EFFECTIVE(Integer.valueOf("0"), "未生效"),
    EFFECTIVE(Integer.valueOf("1"), "已生效"),
    RETURN(Integer.valueOf("2"), "已退回"),
    LOGOUT(Integer.valueOf("3"), "已注销");

    private Integer value;

    private String desc;

    RoleStatusEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

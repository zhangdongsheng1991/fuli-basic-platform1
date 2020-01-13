package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.constant.DateConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 */
@Data
@TableName("system_homepage_module")
public class SystemHomepageModuleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 首页服务模块表
     */
    private Integer id;

    /**
     * 所属服务模块
     */
    private Integer moduleId;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 区分列；1-左，2-中，3-右
     */
    private Integer type;

    /**
     * 状态；1-开启，2-关闭
     */
    private Integer state;

    /**
     * 是否默认模块；0-是，1-否
     */
    private Integer isDefault;

    /**
     * 操作人
     */
    private String operationAccount;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date createTime;

    /**
     * 备注
     */
    private String remark;

    public SystemHomepageModuleDO() {
    }

}
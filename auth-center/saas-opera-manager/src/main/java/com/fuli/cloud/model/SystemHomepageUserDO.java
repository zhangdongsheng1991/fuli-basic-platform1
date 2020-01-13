package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.constant.DateConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  首页服务模块与用户中间表
 * @author WFZ 2019-07-31
 */
@Data
@TableName("system_homepage_user")
public class SystemHomepageUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 首页服务模块表
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 首页模块id
     */
    private Integer homepageModuleId;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 区分列；1-左，2-中，3-右
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    public SystemHomepageUserDO() {
    }

}
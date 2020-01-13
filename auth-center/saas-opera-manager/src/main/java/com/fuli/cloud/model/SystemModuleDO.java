package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  悬浮窗口自定义
 * @author xq 2019-07-30
 */
@Data
@TableName("system_module")
public class SystemModuleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 模块菜单表 - 用户id
     */
    private Integer id;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 排序值
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createTime;

    public SystemModuleDO() {
    }

}

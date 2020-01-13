package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * menu
 * @author 
 */
@Data
@TableName("system_menu")
public class SystemMenuDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单表
     */
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 模块id
     */
    private Integer moduleId;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 菜单标识对应前端页面
     */
    private String label;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 上级id
     */
    private Integer parentId;

    /**
     * 菜单类型 0 菜单 1 按钮
     */
    private Integer type;

    /**
     * 菜单排序升序
     */
    private Integer sort;

    /**
     * 跳转url
     */
    private String url;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public SystemMenuDO() {
    }


}
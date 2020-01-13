package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 */
@Data
public class MenuTreeVO {

    @ApiModelProperty("菜单表")
    private Integer id;

    @ApiModelProperty("模块id")
    private Integer moduleId;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("菜单标识对应前端页面")
    private String label;

    @ApiModelProperty("菜单图标")
    private String icon;

    @ApiModelProperty("上级id")
    private Integer parentId;

    @ApiModelProperty("菜单路径")
    private String path;

    @ApiModelProperty("菜单类型 0 菜单 1 按钮")
    private Integer type;

    @ApiModelProperty("菜单排序升序")
    private Integer sort;

    @ApiModelProperty("跳转url")
    private String url;

    private Date updateTime;

    @ApiModelProperty(value = "是否选中")
    private Boolean checked;

    @ApiModelProperty(value = "子集")
    private List<MenuTreeVO> children;

}

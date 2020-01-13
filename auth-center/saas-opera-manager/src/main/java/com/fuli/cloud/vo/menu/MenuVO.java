package com.fuli.cloud.vo.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:    菜单响应vo
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 10:22
 * @Version:        1.0
*/
@Data
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "模块id")
    private Integer moduleId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "菜单标识对应前端页面")
    private String label;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "上级id")
    private Integer parentId;

    @ApiModelProperty(value = "请求URL",hidden = true)
    private String url;

    @ApiModelProperty(value = "是否选中")
    private Boolean checked;

    @ApiModelProperty(value = "子集")
    private List<MenuVO> children;

}

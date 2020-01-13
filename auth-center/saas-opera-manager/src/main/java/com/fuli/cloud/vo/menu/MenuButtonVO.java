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
public class MenuButtonVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(name = "moduleId" ,value = "菜单id/服务id")
    private Integer moduleId;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "模块名称")
    private String moduleName;

    @ApiModelProperty(value = "菜单标识对应前端页面")
    private String label;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "上级id")
    private Integer parentId;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

}

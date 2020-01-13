package com.fuli.cloud.vo.homepage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:    首页模块响应vo
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 10:22
 * @Version:        1.0
 */
@Data
public class HomepageModuleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模块表主键")
    private Integer homepageModuleId;

    @ApiModelProperty(value = "所属服务id")
    private Integer moduleId;

    @ApiModelProperty(value = "模块名称")
    private String name;

    @ApiModelProperty(value = "菜单标识")
    private String label;

    @ApiModelProperty(value = "区分列；1-左，2-中，3-右")
    private Integer type;

    @ApiModelProperty(value = "排序值")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

}

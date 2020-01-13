package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 模块id与名称对应
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class ModuleMapDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "模块id",example = "10",required = true)
    @NotNull(message = "模块id不能为空")
    private Integer moduleId;

    @ApiModelProperty(value="模块名称",example = "薪发放",required = true)
    @NotNull(message = "模块名称不能为空")
    private String moduleName;


}

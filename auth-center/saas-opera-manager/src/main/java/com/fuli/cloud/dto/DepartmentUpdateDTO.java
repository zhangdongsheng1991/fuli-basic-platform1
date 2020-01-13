package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author zenglw
 * @description:
 * @date 2019/6/27 16:14
 */
@Data
@ToString(callSuper = true)
public class DepartmentUpdateDTO extends DepartmentAddDTO {
    @ApiModelProperty(name = "id",value = "部门ID")
    @NotNull(message = "参数id不能为空")
    private Integer id;

}

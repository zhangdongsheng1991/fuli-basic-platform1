package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author zenglw
 * @description:
 * @date 2019/6/27 16:10
 */
@Data
@ToString
public class DepartmentAddDTO implements Serializable {
    @ApiModelProperty(value = "部门名称",required = true)
    @NotBlank(message = "部门名称不能为空")
    //@Length(min = 1,max = 20, message = "部门名称长度为{min}-{max}个字符")
//    @Pattern(regexp = DepartmentConstant.DEPT_NAME_REGEX, message = DepartmentConstant.DEPART_NAME_NOT_MATCH_DESC)
    @Pattern(regexp = "^(?![\\.\\_\\-\\(\\)]+$)[0-9A-Za-z\\u4e00-\\u9fa5\\.\\_\\-\\(\\)]{1,20}$"
            ,message = "部门名称只支持中文、英文、数字、符号（._-/），不可全部为符号，最长20个字符")
    private String name;

    @ApiModelProperty(value = "部门编码")
    @Pattern(regexp = "^[0-9a-zA-Z]{0,20}$",message = "部门编码只支持英文、数字，最长20个字符")
    private String code;

    @ApiModelProperty(value = "上级部门")
    private Integer parentId;

}

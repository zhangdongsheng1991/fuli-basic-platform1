package com.fuli.cloud.dto;

import com.fuli.cloud.constant.ValidateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色表
 * @author xq 2019-07-29
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("角色表")
public class QuerySystemRoleDTO extends PageDTO implements Serializable {

    @ApiModelProperty(value = "角色名", example = "超级管理", required = true)
//    @NotBlank(message = ValidateConstant.ROLE_NAME_NOT_BLANK_MSG)
    @Length(max = ValidateConstant.ROLE_NAME_LENGTH_MAX, message = ValidateConstant.ROLE_NAME_RULE_MSG)
    @Pattern(regexp = ValidateConstant.ROLE_NAME_RULE, message = ValidateConstant.ROLE_NAME_RULE_MSG)
    private String name;

    @ApiModelProperty("角色状态 0 未生效 1 已生效 2 已退回 3 已注销")
    @Min(value = 0, message = "角色状态类型不合法")
    @Max(value = 3, message = "角色状态类型不合法")
    private Integer status;
}
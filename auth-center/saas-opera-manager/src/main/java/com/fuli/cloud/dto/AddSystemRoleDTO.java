package com.fuli.cloud.dto;

import com.fuli.cloud.constant.ValidateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.Set;

/**
 * @author xq
 * 添加角色封装
 */
@Data
public class AddSystemRoleDTO {

    @ApiModelProperty(value = "角色名", example = "超级管理", required = true)
    @NotBlank(message = ValidateConstant.ROLE_NAME_NOT_BLANK_MSG)
    @Length(max = ValidateConstant.ROLE_NAME_LENGTH_MAX, message = ValidateConstant.ROLE_NAME_RULE_MSG)
    @Pattern(regexp = ValidateConstant.ROLE_NAME_RULE, message = ValidateConstant.ROLE_NAME_RULE_MSG)
    private String name;

    @ApiModelProperty(value = "角色描述", example = "这是超级管理")
    @Length(max = ValidateConstant.ROLE_DESC_LENGTH, message = ValidateConstant.ROLE_DESC_LENGTH_MSG)
    private String description;

    @ApiModelProperty(value = "菜单id集合", example = "[1,2,3]", required = true)
    @NotEmpty(message = ValidateConstant.MENU_NOT_EMPTY)
    private Set<Integer> menuIds;

    @ApiModelProperty(value = "首页模块id集合", example = "[1,2,3]")
    private Set<Integer> homepageModuleIds;

}

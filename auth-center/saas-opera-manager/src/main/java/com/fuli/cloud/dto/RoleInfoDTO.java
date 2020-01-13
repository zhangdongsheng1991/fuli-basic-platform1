package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/7/30
 */
@Data
public class RoleInfoDTO implements Serializable {

    @Size(min = 1, max = 5000, message = "一次批量操作不能超过{max}")
    @NotEmpty(message = "员工id集合不能为空")
    @NotNull(message = "员工id集合不能为空")
    private Set<Integer> userIds;

    @NotNull(message = "是否开通运营权限标记必填")
    @Min(value = 0, message = "是否开通运营权限标记不合法")
    @Max(value = 1, message = "是否开通运营权限标记不合法")
    @ApiModelProperty(value = "是否开通运营权限 0-不开通，1-开通", required = true)
    private Integer openSystem;

    @ApiModelProperty("角色id集合, 若开通运营权限则角色Id集合不能为空")
    private Set<Integer> roleIds;
}

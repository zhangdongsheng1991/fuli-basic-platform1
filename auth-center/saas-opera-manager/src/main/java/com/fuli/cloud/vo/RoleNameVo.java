package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Description: 包含userId和roleName
 * </p>
 *
 * @author chenyi
 * @date 2019年7月2日上午10:16:44
 */
@Data
public class RoleNameVo {

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("角色名称")
    private String roleName;
}

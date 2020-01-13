package com.fuli.cloud.vo;

import com.fuli.cloud.vo.menu.AuthModuleVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 */
@Data
public class SystemRoleDetailsVo {

    @ApiModelProperty("角色id")
    private Integer id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色描述")
    private String description;

    @ApiModelProperty("权限模块信息")
    private List<AuthModuleVO> authModules;

}

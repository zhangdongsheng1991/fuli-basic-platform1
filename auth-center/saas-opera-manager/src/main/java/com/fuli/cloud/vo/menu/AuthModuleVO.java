package com.fuli.cloud.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fuli.cloud.model.SystemHomepageModuleDO;
import com.fuli.cloud.model.SystemMenuDO;
import com.fuli.cloud.vo.HomePageModuleVo;
import com.fuli.cloud.vo.MenuTreeVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限模块信息
 * @author pcg
 * @date 2019-6-26 17:17:41
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthModuleVO {


    @ApiModelProperty("模块id")
    private Integer id;

    @ApiModelProperty("模块名称")
    @JsonProperty("modelName")
    private String moduleName;

    @ApiModelProperty("权限菜单")
    private List<MenuTreeVO> menus;

    @ApiModelProperty("工作台数据权限")
    private List<HomePageModuleVo> homepageModules;
}
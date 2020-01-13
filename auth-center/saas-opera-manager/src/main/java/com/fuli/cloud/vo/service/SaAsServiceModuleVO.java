package com.fuli.cloud.vo.service;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:    第三方服务模块响应vo
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class SaAsServiceModuleVO implements Serializable {

    @ApiModelProperty(value = "服务id")
    private Integer id;

    @ApiModelProperty(value = "菜单模块id")
    private Integer moduleId;

    @ApiModelProperty(value = "服务名称")
    private String name;

    @ApiModelProperty(value = "服务logo--已开通")
    private String logoUrl;

    @ApiModelProperty(value = "服务logo--未开通")
    private String logoUrlNot;

}

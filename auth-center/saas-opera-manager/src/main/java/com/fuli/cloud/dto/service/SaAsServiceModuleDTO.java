package com.fuli.cloud.dto.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 现有第三方服务模块记录
 * @author WFZ 2019-07-30
 */
@Data
@ApiModel("现有第三方服务模块记录")
public class SaAsServiceModuleDTO {

    @ApiModelProperty("现有第三方服务模块记录表 -- 主键id")
    private Integer id;

    @ApiModelProperty("对应权限的模块id")
    private Integer moduleId;

    @ApiModelProperty("模块名称")
    private String name;

    @ApiModelProperty("服务状态：1-开启，2-关闭")
    private Integer state;

    @ApiModelProperty("是否第三方服务：1-是，0-否")
    private Integer isOther;

    @ApiModelProperty("标识，对应前端页面")
    private String label;

    @ApiModelProperty("服务logo地址-已开通")
    private String logoUrl;

    @ApiModelProperty("服务logo地址-未开通")
    private String logoUrlNot;

    @ApiModelProperty("创建人")
    private Integer createUser;

    @ApiModelProperty("创建时间")
    private Date createTime;

    public SaAsServiceModuleDTO() {
    }

}

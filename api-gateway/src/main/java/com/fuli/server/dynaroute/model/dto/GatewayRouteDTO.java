package com.fuli.server.dynaroute.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author: XYJ
 * @Date: 2019/8/5 15:01
 * 应用基础信息
 */

@Data
public class GatewayRouteDTO implements Serializable {
    private static final long serialVersionUID = -4666067795040232681L;

    private String routeId;
    //路由名称
    @ApiModelProperty(value = "路由名称", required = true)
    @NotNull(message = "routeName 不能为空")
    private String routeName;
    //路径
    @ApiModelProperty(value = "路由", required = true)
    @NotNull(message = "path 不能为空")
    private String path;
    //    服务ID
    @ApiModelProperty(value = "服务id", required = true)
    @NotNull(message = "serviceId 不能为空")
    private String serviceId;
    //    完整地址
    @ApiModelProperty(value = "服务id", required = false)
    private String url;
    //    忽略前缀
    @ApiModelProperty(value = "忽略前缀", required = true)
    @NotNull(message = "stripPrefix 不能为空")
    private String stripPrefix="1";
    //    0-不重试 1-重试
    @ApiModelProperty(value = " 0-不重试 1-重试", required = false)
    private String retryable;
    //    状态:0-无效 1-有效
    @ApiModelProperty(value = "状态:0-无效 1-有效", required = true)
    @NotNull(message = "status 不能为空")
    private String status="1";
    //    是否为保留数据:0-否 1-是
    @ApiModelProperty(value = "服务id", required = true)
    @NotNull(message = "isPersist 不能为空")
    private String isPersist="1";
    //    路由说明
    @ApiModelProperty(value = " 路由说明", required = false)
    private String routeDesc;

}

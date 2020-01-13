package com.fuli.server.model.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
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

    @ApiModelProperty(value = "路由名称", required = true,example = "授权基础服务")
    @NotEmpty(message = "routeName 不能为空")
    private String routeName;

    @ApiModelProperty(value = "路由，参考值：- Path=/base-auth/**", required = true ,example = "/base-auth/**")
    @NotEmpty(message = "path 不能为空")
    private String path;

    @ApiModelProperty(value = "服务id，nacos里注册的服务名", required = true ,example = "base-auth")
    @NotEmpty(message = "serviceId 不能为空")
    private String serviceId;

    @ApiModelProperty(value = "外部接口完整路径", required = false)
    private String url;

    @ApiModelProperty(value = "忽略前缀", required = true , example = "1")
    @NotEmpty(message = "stripPrefix 不能为空")
    private String stripPrefix="1";

    @ApiModelProperty(value = " 0-不重试 1-重试", required = false,example = "1")
    private String retryable="1";

    @ApiModelProperty(value = "状态:0-无效 1-有效", required = true , example = "1")
    @NotEmpty(message = "status 不能为空")
    private String status="1";

    @ApiModelProperty(value = "是否为保留数据:0-否 1-是", required = true , example = "1")
    @NotEmpty(message = "isPersist 不能为空")
    private String isPersist="1";

    @ApiModelProperty(value = " 路由说明", required = false)
    private String routeDesc;

}

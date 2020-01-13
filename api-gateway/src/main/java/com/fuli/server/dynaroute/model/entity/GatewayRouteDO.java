package com.fuli.server.dynaroute.model.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: XYJ
 * @Date: 2019/8/5 15:01
 * 应用基础信息
 */
@TableName("gateway_route")
@Data
public class GatewayRouteDO implements Serializable {
    private static final long serialVersionUID = -4666067795040232681L;

    @TableId(type = IdType.INPUT)
    private String routeId;
    //路由名称
    private String routeName;
    //路径
    private String path;
    //    服务ID
    private String serviceId;
    //    完整地址
    private String url;
    //    忽略前缀
    private String stripPrefix;
    //    0-不重试 1-重试
    private String retryable;
    //    状态:0-无效 1-有效
    private String status;
    //    是否为保留数据:0-否 1-是
    private String isPersist;
    //    路由说明
    private String routeDesc;

}

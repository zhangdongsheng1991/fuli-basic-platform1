package com.fuli.server.dynaroute.controller;

import com.fuli.server.dynaroute.DynamicRouteHandler;
import com.fuli.server.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author create by XYJ
 * @Date 2019/9/3 18:13
 **/
@Api(tags = "动态路由")
@RestController
@RequestMapping("/route")
public class RouteController {



    @Autowired
    private DynamicRouteHandler dynamicRouteHandler;

    /**
     * 刷新路由配置
     *
     * @return
     */
    @ApiOperation(value = "刷新路由")
    @GetMapping("/refresh")
    public Result refresh() throws Exception {
        this.dynamicRouteHandler.loadRouteConfig();
        return Result.success("路由刷新成功");
    }


}

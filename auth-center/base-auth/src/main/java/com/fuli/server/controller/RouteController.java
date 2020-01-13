package com.fuli.server.controller;

import com.fuli.logtrace.annotation.LogTrace;
import com.fuli.server.base.Result;
import com.fuli.server.model.dto.GatewayRouteDTO;
import com.fuli.server.service.impl.GatewayRouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author create by XYJ
 * @Date 2019/9/3 18:13
 **/
@Api(tags = "动态路由")
@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private GatewayRouteService gatewayRouteService;


    /**
     * 增加路由记录
     *
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "新增路由")
    @LogTrace("新增路由")
    public Result add(@RequestBody @Validated GatewayRouteDTO gatewayRouteDto) throws Exception {
        gatewayRouteService.add(gatewayRouteDto);
        return Result.succeed();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新路由")
    public Result update( @RequestBody @Validated GatewayRouteDTO gatewayRouteDto) throws Exception {
        gatewayRouteService.update(gatewayRouteDto);
        return Result.succeed();
    }

    @GetMapping("/delete")
    @ApiOperation(value = "删除路由")
    public Result delete(@RequestParam("id") @Validated String id) throws Exception {
        gatewayRouteService.delete(id);
        return Result.succeed();
    }
}

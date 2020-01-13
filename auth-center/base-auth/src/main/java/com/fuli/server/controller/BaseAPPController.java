package com.fuli.server.controller;

import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.server.base.Result;
import com.fuli.server.model.dto.BaseAddAppDTO;
import com.fuli.server.model.entity.BaseAppDO;
import com.fuli.server.service.BaseAPPService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author create by XYJ
 * @Date 2019/8/7 11:31
 **/
@RestController
@RequestMapping("/app")
@Api("应用管理模块")
public class BaseAPPController {

    @Autowired
    private BaseAPPService baseAPPService;

    /**
     * 新增应用
     *
     * @param app
     * @return
     */
    @ApiOperation("新增应用")
    @PostMapping("/add")
    public Result addAPPInfo(@RequestBody BaseAddAppDTO app) {

        BaseAppDO baseApp = baseAPPService.addApp(app);

        return Result.succeed(baseApp);
    }

    /**
     * 根据应用标识获取应用配置详细信息
     *
     * @param clientId
     * @return
     */

    @ApiOperation(value = "根据clientId获取应用配置详细信息", notes = "根据clientId获取应用配置详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true, paramType = "path"),
    })
    @GetMapping("/client/{clientId}/details")
    public Result<CustomClientDetails> getClientDetailsByClientId(@PathVariable("clientId") String clientId) {
        CustomClientDetails clientDetails = baseAPPService.getDetailsByClientId(clientId);
        return Result.succeed(clientDetails);
    }
    /**
     * 根据clientid 获取应用基本信息 ：appname
     *
     * @param clientId
     * @return
     */

    @ApiOperation(value = "根据clientid 获取应用基本信息(只返回appid与appname)", notes = "根据clientid 获取应用基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "clientId", value = "应用ID", defaultValue = "1", required = true),
    })
    @GetMapping("/client/info")
    public Result<Object> getInfoByClientId(@RequestParam("clientId") String clientId) {
        BaseAppDO appInfo = baseAPPService.getInfoByClientId(clientId);
        Map<String,String> map=new HashMap<>();
        map.put("appId",appInfo.getApiKey());
        map.put("appName",appInfo.getAppName());
        map.put("appNameEn",appInfo.getAppNameEn());
        map.put("id",appInfo.getAppId());
        return Result.succeed(map);
    }

}

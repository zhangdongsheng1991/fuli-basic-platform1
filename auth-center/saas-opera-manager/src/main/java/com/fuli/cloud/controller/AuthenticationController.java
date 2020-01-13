package com.fuli.cloud.controller;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.utils.RedisService;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description:    用户鉴权
 * @Author:         WFZ
 * @CreateDate:     2019/7/30 10:49
 * @Version:        1.0
*/
@Slf4j
@RestController
@RequestMapping("/check")
@Api(tags="用户鉴权")
public class AuthenticationController extends BaseController {

    @Autowired
    AuthenticationService authenticationService;
    @Autowired
    private RedisService redisService;

    @ApiIgnore
    @PostMapping("/requestUrl")
    @ApiOperation("url鉴权--温福州")
    public Result<Boolean> requestUrl(@RequestParam("userId") String userId, @RequestParam("url")String url){
        return authenticationService.requestUrl(userId,url);
    }

}

package com.fuli.cloud.controller;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.service.RedisManageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Description:    缓存管理Controller
 * @Author:         WFZ
 * @CreateDate:     2019/9/11 10:24
 * @Version:        1.0
 */
@ApiIgnore
@RestController
@RequestMapping("/redis")
@Api(tags = "缓存管理")
public class RedisManageController {

    @Autowired
    private RedisManageService redisManageService;
    /**
     * SaaS门户个人鉴权redis初始化
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 11:37
     */
    @ApiIgnore
    @PostMapping("/authenticationInit")
    public Result authenticationInit(){
        redisManageService.authenticationInit();
        return Result.succeed();
    }


    /**
     * 菜单redis初始化
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 11:37
     */
    @ApiIgnore
    @PostMapping("/menuInit")
    public Result menuInit(){
        redisManageService.updateRedisMenu();
        return Result.succeed();
    }


}

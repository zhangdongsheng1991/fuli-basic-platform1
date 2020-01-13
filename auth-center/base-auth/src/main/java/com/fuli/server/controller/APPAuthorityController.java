package com.fuli.server.controller;

import com.fuli.server.base.Result;
import com.fuli.server.service.AppAuthorityService;
import com.fuli.server.service.AuthorityApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用权限控制器
 *
 * @Author create by XYJ
 * @Date 2019/8/16 16:20
 **/
@RestController
@RequestMapping("/authority")
public class APPAuthorityController {

    @Autowired
    private AuthorityApiService authorityApiService;

    /**
     * 权限绑定api
     *
     * @return
     */
    @PostMapping("/api/bind")
    public Result authorityBindApi(String groupId, String apiIds) {
        Integer updateCount = authorityApiService.authorityBindApi(groupId, apiIds.split(","),true);
        return Result.succeed(updateCount);
    }

}

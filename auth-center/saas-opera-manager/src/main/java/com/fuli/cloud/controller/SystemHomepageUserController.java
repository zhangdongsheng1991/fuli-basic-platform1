package com.fuli.cloud.controller;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.homepage.BatchInsertDTO;
import com.fuli.cloud.service.SystemHomepageUserService;
import com.fuli.cloud.vo.homepage.HomepageModuleListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description:    首页模块管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 11:14
 * @Version:        1.0
*/
@Slf4j
@RestController
@RequestMapping("/homepage")
@Api(tags="首页模块管理API --温福州")
public class SystemHomepageUserController extends BaseController {

    @Autowired
    SystemHomepageUserService systemHomepageUserService;

    @PostMapping(value = CommonConstant.KEY_LIST)
    @ApiOperation("获取首页模块列表 --温福州")
    public Result<HomepageModuleListVO> list(){
         return systemHomepageUserService.list(getSystemUser());
    }

    @PostMapping(value = CommonConstant.KEY_BATCH_INSERT)
    @ApiOperation("自定义排序(需传入当前用户拥有的所有模块数据列表) --温福州")
    public Result batchInsert(@RequestBody @Validated BatchInsertDTO homepageUserDto){
        return systemHomepageUserService.batchInsert(getSystemUser(),homepageUserDto.getList());
    }

    @PostMapping(value = "reset")
    @ApiOperation("重置 --温福州")
    public Result reset(){
        return systemHomepageUserService.reset(getSystemUser());
    }
}

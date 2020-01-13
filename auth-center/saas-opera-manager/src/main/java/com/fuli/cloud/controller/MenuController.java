package com.fuli.cloud.controller;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.service.MenuService;
import com.fuli.cloud.vo.menu.MenuButtonVO;
import com.fuli.cloud.vo.menu.MenuVO;
import com.fuli.cloud.vo.SystemModuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Description:    菜单管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 19:40
 * @Version:        1.0
*/
@RestController
@Api(tags = "菜单管理 --温福州")
@RequestMapping("/menu")
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/list")
    @ApiOperation("菜单列表（只获取一二级菜单）--温福州")
    public Result<List<SystemModuleVO>> list(){
        return menuService.listMenuByUserId(getSystemUser());
    }


    @PostMapping("/listButtonByMenuId")
    @ApiOperation("根据菜单id获取拥有的当前页面的按钮--温福州")
    public Result<List<MenuButtonVO>> listButtonByMenuId(String menuId){
        return menuService.listButtonByMenuId(menuId,getSystemUser());
    }


    @PostMapping("/placeMenu")
    @ApiOperation("菜单列表（渠道运营）--温福州")
    public Result<List<SystemModuleVO>> placeMenu(){
        return menuService.placeMenu(getSystemUser());
    }

}

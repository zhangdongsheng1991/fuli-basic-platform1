package com.fuli.cloud.controller;

import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.vo.SystemRoleDetailsVo;
import com.fuli.cloud.vo.SystemRoleListVO;
import com.fuli.cloud.vo.menu.AuthModuleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色管理
 * @author xq
 */
@RestController
@RequestMapping(value = "/systemRole")
@Api(tags = "角色管理--谢庆")
public class SystemRoleController extends BaseController {

    @Autowired
    SystemRoleService systemRoleService;

    /**
     * 角色列表查询
     * @param querySystemRoleDTO 查询条件
     * @return 模糊查询分页结果
     */
    @ApiOperation("角色列表查询")
    @PostMapping("/getSystemRoleListPage")
    public Result<PageResult<SystemRoleListVO>> getSystemRoleListPage(@RequestBody QuerySystemRoleDTO querySystemRoleDTO){
        return systemRoleService.getSystemRoleListPage(querySystemRoleDTO);
    }

    /**
     * 查询角色详情查询
     * @param querySystemRoleInfoDTO 详情
     * @return 查询结果
     */
    @ApiOperation("查询角色详情查询")
    @PostMapping("/getSystemRoleDetails")
    Result<SystemRoleDetailsVo> getSystemRoleInfo(@RequestBody @Validated QuerySystemRoleInfoDTO querySystemRoleInfoDTO){
        return systemRoleService.getSystemRoleInfo(querySystemRoleInfoDTO);
    }

    /**
     * 新增角色模块菜单查询
     * @return 查询结果
     */
    @ApiOperation("新增角色模块菜单查询")
    @PostMapping("/getMenuAndModuleDetails")
    Result<List<AuthModuleVO>> getSystemRole(){
        return Result.succeed(systemRoleService.getAllModule(null , 1 ));
    }

    /**
     * 添加角色
     * @param addSystemRoleDTO 查询条件
     * @return 模糊查询分页结果
     */
    @ApiOperation("添加角色")
    @PostMapping("/saveSystemRole")
    public Result saveSystemRole(@RequestBody @Validated AddSystemRoleDTO addSystemRoleDTO){
        return systemRoleService.saveSystemRole(addSystemRoleDTO ,getTokenUser());
    }


    /**
     * 修改角色
     * @param updateSystemRoleDTO 修改角色
     * @return 修改jieguo
     */
    @ApiOperation("修改角色")
    @PostMapping("/updateSystemRole")
    Result updateSystemRole(@RequestBody @Validated UpdateSystemRoleDTO updateSystemRoleDTO){
        return systemRoleService.updateSystemRole(updateSystemRoleDTO ,getTokenUser());
    }

    /**
     * 注销角色
     * @param logoutSystemRoleDTO 注销角色
     * @return 注销角色
     */
    @ApiOperation("注销角色")
    @PostMapping("/logoutSystemRole")
    Result logoutSystemRole(@RequestBody @Validated LogoutSystemRoleDTO logoutSystemRoleDTO){
        return systemRoleService.logoutSystemRole(logoutSystemRoleDTO ,getTokenUser());
    }

    /**
     * 获取用户信息
     * @return 当前登录用户信息
     */
    private TokenUser getTokenUser(){
        // 修改成从token中获取用户信息
        return getSystemUser();
    }

}

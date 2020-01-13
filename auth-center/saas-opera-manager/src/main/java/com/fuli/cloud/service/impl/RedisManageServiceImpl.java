package com.fuli.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.utils.RedisService;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.service.RedisManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @Description:    缓存管理Service
 * @Author:         WFZ
 * @CreateDate:     2019/9/12 11:10
 * @Version:        1.0
 */
@Service
public class RedisManageServiceImpl implements RedisManageService {

    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private RedisService redisService;


    @Override
    public Result authenticationInit() {
        redisService.remove("saAsOperationMenu");
        redisService.removePattern("saAsOperationRoleMenu:*");
        redisService.removePattern("saAsOperationUserRole:*");
        // 写入redis缓存
        // 菜单
        List<Map<String, String>> list = systemRoleMapper.queryAllMenu();
        redisService.set("saAsOperationMenu", JSONObject.toJSONString(list));
        // 角色菜单
        List<Map<String, String>> roleMenu = systemRoleMapper.queryAllRoleMenu(null);
        roleMenu.forEach(p ->redisService.set("saAsOperationRoleMenu:"+String.valueOf(p.get("roleId")),p.get("menuId")));
        // 用户角色
        List<Map<String, String>> userRole = systemRoleMapper.queryAllUserRole();
        userRole.forEach(p ->redisService.set("saAsOperationUserRole:"+String.valueOf(p.get("userId")),p.get("roleId")));
        return Result.succeed();
    }

    /**
     * 菜单redis修改
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    @Override
    public Result updateRedisMenu(){
        redisService.remove("saAsOperationMenu");
        // 菜单
        List<Map<String, String>> list = systemRoleMapper.queryAllMenu();
        redisService.set("saAsOperationMenu", JSONObject.toJSONString(list));
        return Result.succeed();
    }

    /**
     * 角色新增/修改更新redis
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    @Override
    public Result roleAddORUpdate(Integer roleId){
        (systemRoleMapper.queryAllRoleMenu(roleId)).forEach(p ->redisService.set("saAsOperationRoleMenu:"+String.valueOf(p.get("roleId")),p.get("menuId")));
        return Result.succeed();
    }


    /**
     * 角色禁用/注销更新redis
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    @Override
    public Result roleDisable(Integer roleId){
        redisService.remove("saAsOperationUserRole:"+String.valueOf(roleId));
        redisService.remove("saAsOperationRoleMenu:"+String.valueOf(roleId));
        return Result.succeed();
    }

    /**
     * 用户角色修改更新redis
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    @Override
    public Result userRoleUpdate(){
        redisService.removePattern("saAsOperationUserRole:*");
        // 用户角色
        (systemRoleMapper.queryAllUserRole()).forEach(p ->redisService.set("saAsOperationUserRole:"+String.valueOf(p.get("userId")),p.get("roleId")));
        return Result.succeed();
    }
}

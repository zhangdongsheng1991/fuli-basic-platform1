package com.fuli.cloud.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.commons.utils.RedisService;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.mapper.SystemMenuMapper;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.model.SystemMenuDO;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.service.AuthenticationService;
import com.fuli.cloud.vo.menu.MenuVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Description:    菜单接口逻辑实现层
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 20:39
 * @Version:        1.0
*/
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemMenuMapper systemMenuMapper;
    @Autowired
    private RedisService redisService;


    /**
     * url鉴权
     *
     * @param userId : 用户id
     * @param url    : 请求URL
     * @return Result
     * @author WFZ
     * @date 2019/6/26 10:29
     */
    @Override
    public Result<Boolean> requestUrl(String userId, String url) {
        if (PublicUtil.isEmpty(userId)){
            return Result.failedWith(null,CodeEnum.PARAM_ERROR.getCode(),"用户id不能为空");
        }
        if (PublicUtil.isEmpty(url)){
            return Result.failedWith(null,CodeEnum.PARAM_ERROR.getCode(),"url不能为空");
        }
        /** 首先判断是否超级管理员--查出角色 */
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(userId);
        /** 记录角色id*/
        Set<Integer> roles = new HashSet<>(4);
        for (SystemRoleDO role : roleList){
            roles.add(role.getId());
            if (role.getAdministrators().intValue()==1){
                /** 超级管理员直接放行*/
                return Result.succeed(true);
            }
        }

        /** 根据URL获取菜单，如果没有说明不需要拦截*/
        List<SystemMenuDO> systemMenuList = systemMenuMapper.findByUrl(url);
        if (CollectionUtils.isEmpty(systemMenuList)){
            return Result.succeed(true);
        }

        /** 用户拥有的菜单*/
        List<MenuVO> menuList = new ArrayList<>();
        /** 先判断Redis有没有*/
        if (redisService.exists(CommonConstant.MENU_REDIS_KEY + userId)){
            Object object = redisService.get(CommonConstant.MENU_REDIS_KEY + userId);
            List<Map> menuLists = JSONObject.parseObject(object.toString(), List.class);
            for (Map m : menuLists){
                menuList.add(JSONObject.parseObject(m.toString(),MenuVO.class));
            }
        }else {
            menuList = systemMenuMapper.listMenuByRoles(roles,null);
            /** 保存到redis,15分钟*/
            redisService.set(CommonConstant.MENU_REDIS_KEY + userId,JSONObject.toJSONString(menuList), 900L);
        }
        if (CollectionUtils.isEmpty(menuList)){
            return Result.succeed(false);
        }

        for (MenuVO vo : menuList){
            for (SystemMenuDO menuDO : systemMenuList){
                if (vo.getId().intValue() == menuDO.getId().intValue()){
                    return Result.succeed(true);
                }
            }
        }
        /** 无权限*/
        return Result.succeed(false);
    }
}

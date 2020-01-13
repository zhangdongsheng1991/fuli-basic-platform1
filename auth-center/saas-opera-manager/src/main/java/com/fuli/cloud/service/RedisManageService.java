package com.fuli.cloud.service;


import com.fuli.cloud.commons.Result;

/**
 * @Description:    缓存管理Service
 * @Author:         WFZ
 * @CreateDate:     2019/9/11 12:29
 * @Version:        1.0
 */
public interface RedisManageService {

    /**
     * aaS门户个人鉴权redis初始化
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:35
     */
    Result authenticationInit();


    /**
     * 菜单redis修改
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    Result updateRedisMenu();

    /**
     * 角色新增/修改更新redis
     * @author      WFZ
     * @param       roleId
     * @return      Result
     * @date        2019/9/11 12:37
     */
    Result roleAddORUpdate(Integer roleId);

    /**
     * 角色禁用/注销更新redis
     * @author      WFZ
     * @param       roleId
     * @return      Result
     * @date        2019/9/11 12:37
     */
    Result roleDisable(Integer roleId);

    /**
     * 用户角色修改更新redis
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/11 12:37
     */
    Result userRoleUpdate();

}

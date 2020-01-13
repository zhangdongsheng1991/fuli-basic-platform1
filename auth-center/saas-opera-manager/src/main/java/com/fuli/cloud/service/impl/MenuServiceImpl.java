package com.fuli.cloud.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.utils.JacksonUtil;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.SystemMenuMapper;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.mapper.SystemRoleMenuMapper;
import com.fuli.cloud.model.*;
import com.fuli.cloud.service.MenuService;
import com.fuli.cloud.vo.MenuTreeVO;
import com.fuli.cloud.vo.menu.MenuButtonVO;
import com.fuli.cloud.vo.menu.MenuVO;
import com.fuli.cloud.vo.SystemModuleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


/**
 * @Description: 菜单接口逻辑实现层
 * @Author: WFZ
 * @CreateDate: 2019/6/26 20:39
 * @Version: 1.0
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemMenuMapper systemMenuMapper;

    @Resource
    SystemRoleMenuMapper systemRoleMenuMapper;

    /**
     * 根据企业id和用户id获取菜单，只获取菜单不获取按钮
     *
     * @param user : 登录用户
     * @return Result
     * @author WFZ
     * @date 2019/6/26 10:29
     */
    @Override
    public Result listMenuByUserId(TokenUser user) {
        /** 首先判断是否超级管理员--查出角色 */
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(user.getId());
        if (null != roleList && roleList.size() > 0) {
            boolean flag = false;
            /** 记录角色id*/
            Set<Integer> roles = new HashSet<>(4);
            for (SystemRoleDO role : roleList) {
                roles.add(role.getId());
                if (role.getAdministrators().intValue() == 1) {
                    flag = true;
                }
            }
            /** 获取拥有的模块*/
            List<SystemModuleVO> serviceList = systemMenuMapper.listSystemModuleByRoles(flag ? null : roles, 2);
            if (null != serviceList && serviceList.size() > 0) {
                List<MenuVO> menuList = new ArrayList<>();
                if (flag) {
                    /** 超级管理员*/
                    menuList = systemMenuMapper.listMenuByRoles(null, 0);
                } else {
                    /** 普通角色*/
                    menuList = systemMenuMapper.listMenuByRoles(roles, 0);
                }
                if (null != menuList && menuList.size() > 0) {
                    return Result.succeed(dataTransformation(menuList, serviceList));
                }
            }
        }
        return Result.succeed();
        /*return Result.failed(CodeEnum.ACCOUNT_NO_PERMISSIONS_EXCEPTION);*/
    }


    /**
     * 获取渠道运营菜单
     * @author      WFZ
     * @param 	    user : 登录用户
     * @return      Result
     * @date        2019/6/26 10:29
     */
    @Override
    public Result placeMenu(TokenUser user) {
        /** 首先判断是否超级管理员--查出角色 */
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(user.getId());
        if (null != roleList && roleList.size() > 0) {
            /** 记录角色id*/
            Set<Integer> roles = new HashSet<>(4);
            boolean flag = false;
            for (SystemRoleDO role : roleList) {
                roles.add(role.getId());
                if (role.getAdministrators().intValue() == 1) {
                    flag = true;
                }
            }
            /** 获取拥有的模块*/
            List<MenuVO> menuList = systemMenuMapper.listMenuByRoles(flag ? null:roles, 2);

            List<SystemModuleVO> serviceList = new ArrayList<>();
            SystemModuleVO vo = new  SystemModuleVO();
            vo.setId(2);
            vo.setModelName("渠道门户");
            serviceList.add(vo);
            if (null != menuList && menuList.size() > 0) {
                return Result.succeed(dataTransformation(menuList, serviceList));
            }
        }
        return Result.succeed();
    }


    /**
     * 数据转化
     *
     * @param list        : 菜单数据
     * @param serviceList : 企业开通的服务
     * @return Result
     * @author WFZ
     * @date 2019/6/26 20:49
     */
    private List<SystemModuleVO> dataTransformation(List<MenuVO> list, List<SystemModuleVO> serviceList) {
        /** 记录返回的数据*/
        if (!serviceList.isEmpty()) {
            for (SystemModuleVO maps : serviceList) {
                /** 记录菜单数据*/
                List<MenuVO> menuList = new ArrayList<>();
                for (MenuVO vo : list) {
                    if (maps.getId().intValue() == vo.getModuleId().intValue()) {
                        menuList.add(vo);
                    }
                }
                maps.setChildren(changeTree(menuList));
            }
        }
        return serviceList;
    }

    /**
     * 生成树形结果数据
     *
     * @param list : 菜单数据
     * @return Result
     * @author WFZ
     * @date 2019/6/26 20:49
     */
    private List<MenuVO> changeTree(List<MenuVO> list) {
        List<MenuVO> menuList = new ArrayList<>();
        /** 循环取出一级菜单*/
        for (MenuVO vo : list) {
            if (null == vo.getParentId() || vo.getParentId() == 0) {
                menuList.add(vo);
            }
        }
        /** 循环取出二级菜单*/
        for (MenuVO menu : menuList) {
            List<MenuVO> childrenList = new ArrayList<>();
            for (MenuVO vo : list) {
                if (menu.getId().intValue() == vo.getParentId().intValue()) {
                    childrenList.add(vo);
                }
            }
            menu.setChildren(childrenList);
        }

        return menuList;
    }


    /**
     * 根据菜单id获取拥有的当前页面的按钮
     *
     * @param menuId
     * @return Result
     * @author WFZ
     * @date 2019/7/31 14:53
     */
    @Override
    public Result listButtonByMenuId(Serializable menuId, TokenUser user) {
        /** 首先判断是否超级管理员--查出角色 */
        List<SystemRoleDO> roleList = systemRoleMapper.listSystemRoleByUserId(user.getId());
        if (PublicUtil.isNotEmpty(roleList)) {
            boolean flag = false;
            /** 记录角色id*/
            Set<Integer> roles = new HashSet<>(8);
            for (SystemRoleDO role : roleList) {
                roles.add(role.getId());
                if (role.getAdministrators().intValue() == 1) {
                    flag = true;
                }
            }
            /** 获取拥有的模块*/
            List<MenuButtonVO> menuList = new ArrayList<>();
            if (flag) {
                /** 超级管理员*/
                menuList = systemMenuMapper.listButtonByMenuId(menuId, null);
            } else {
                /** 普通角色*/
                menuList = systemMenuMapper.listButtonByMenuId(menuId, roles);
            }
            return Result.succeed(menuList);
        }

        return Result.succeed();
    }

    /**
     * 功能描述: 查询出所有菜单<br>
     * 〉
     *
     * @param moduleId 模块id
     * @param roleId   角色（不传时查询模块下菜单）
     * @param type     查询类型 〈0 查看(只展示已有菜单)  1 编辑（已选菜单与未选菜单）  2 新增（所有菜单）
     * @return List<MenuTreeVO>
     * @author XQ
     * @date 2019/8/1 10:00
     */
    @Override
    public List<MenuTreeVO> getAllMenuTree(Integer moduleId, Integer roleId, Integer type) {
        //选中的菜单
        Set<Integer> checkedMenu = new HashSet<>();
        Map<Integer, Integer> maps = new HashMap<>(8);
        if (roleId != null) {
            // 角色工作台关联
            List<SystemRoleMenuDO> systemRoleMenuList = systemRoleMenuMapper.selectList(new QueryWrapper<SystemRoleMenuDO>().eq("role_id", roleId));
            if (systemRoleMenuList == null || systemRoleMenuList.size() < 1) {
                throw new CustomException(CodeEnum.HOME_PAGE_ID_ERROR);
            }
            //选中的菜单
            systemRoleMenuList.forEach(roleMenu -> {
                checkedMenu.add(roleMenu.getMenuId());
                maps.put(roleMenu.getMenuId(), roleMenu.getMenuId());
            });

        }
        QueryWrapper<SystemMenuDO> queryWrapper = new QueryWrapper<>();
        Set<Integer> queryList = null;
        // 查看
        if (type.intValue() == 0) {
            queryList = checkedMenu;
            queryWrapper.in(!CollectionUtils.isEmpty(queryList), "id", queryList);
        }
        if (moduleId != null) {
            queryWrapper.eq("module_id", moduleId);
        }
        queryWrapper.orderByAsc("sort");
        List<SystemMenuDO> list = systemMenuMapper.selectList(queryWrapper);
        JacksonUtil.dumnToPrettyJsonInfo("数据库查询到模块下的菜单：", list);
        List<MenuTreeVO> menuTreeVoList = new ArrayList<>();
        list.forEach(
                systemMenuDO -> {
                    MenuTreeVO menuTreeVO = new MenuTreeVO();
                    BeanUtils.copyProperties(systemMenuDO, menuTreeVO);
                    //查看
                    if (type.intValue() == 0) {
                        menuTreeVO.setChecked(true);
                        //编辑
                    } else if (type.intValue() == 0) {
                        if (menuTreeVO.getId().intValue() == maps.get(menuTreeVO.getId()).intValue()) {
                            menuTreeVO.setChecked(true);
                        } else {
                            menuTreeVO.setChecked(false);
                        }
                    } else {
                        menuTreeVO.setChecked(false);
                    }
                    menuTreeVoList.add(menuTreeVO);
                }
        );
        log.info(JSON.toJSONString(list));
        List<MenuTreeVO> tree = new ArrayList<>();
        menuTreeVoList.forEach(menuTreeVO -> {
            /*一级菜单*/
            if (menuTreeVO.getParentId().intValue() == 0) {
                tree.add(menuTreeVO);
            }
            menuTreeVoList.forEach(childMenu -> {
                if (childMenu.getParentId().intValue() == menuTreeVO.getId().intValue()) {
                    if (menuTreeVO.getChildren() == null) {
                        menuTreeVO.setChildren(new ArrayList<>());
                    }
                    menuTreeVO.getChildren().add(childMenu);
                }
            });
        });
        JacksonUtil.dumnToPrettyJsonInfo("生成树结构返回的模块下的菜单：", tree);
        return tree;
    }
}

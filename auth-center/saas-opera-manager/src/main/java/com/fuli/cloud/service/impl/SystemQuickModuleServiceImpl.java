package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseServiceImpl;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.dto.quick.BatchInsertDTO;
import com.fuli.cloud.dto.quick.QuickModuleBatchSortDTO;
import com.fuli.cloud.dto.quick.QuickModuleSortDTO;
import com.fuli.cloud.mapper.SystemMenuMapper;
import com.fuli.cloud.mapper.SystemQuickModuleMapper;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.model.SystemQuickModuleDO;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.MenuService;
import com.fuli.cloud.service.SystemQuickModuleService;
import com.fuli.cloud.vo.SystemModuleVO;
import com.fuli.cloud.vo.menu.MenuVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.*;

/**
 * @Description:    快捷面板接口
 * @Author:         WFZ
 * @CreateDate:     2019/4/26 17:10
 * @Version:        1.0
 */
@Service
public class SystemQuickModuleServiceImpl extends BaseServiceImpl<SystemQuickModuleMapper, SystemQuickModuleDO> implements SystemQuickModuleService{

    private static final Logger log = LoggerFactory.getLogger(SystemQuickModuleServiceImpl.class);

    @Autowired
    MenuService menuService;
    @Autowired
    SystemMenuMapper systemMenuMapper;
    @Autowired
    SystemRoleMapper systemRoleMapper;

    /**
     * 查询数据列表
     *
     * @param userInfo： 用户信息
     * @return Result
     * @author WFZ
     * @date 2019/6/25 11:07
     */
    @Override
    public Result list(TokenUser userInfo) {
        /** 获取已经开通的快捷模块 */
        List<SystemQuickModuleDO> menuList = getOpenQuickModule(userInfo.getId());
        Result result = menuService.listMenuByUserId(userInfo);
        if (PublicUtil.isResultSuccess(result) && PublicUtil.isNotEmpty(result.getData())){
            List<SystemModuleVO> list1 = (List<SystemModuleVO>)result.getData();
            if (PublicUtil.isNotEmpty(list1)){
                for (SystemModuleVO m : list1) {
                    /** 第一层获取服务模块*/
                    if (PublicUtil.isNotEmpty(m.getChildren())){
                        /** 判断模块下面是否有子菜单*/
                        List<MenuVO> list2 = m.getChildren();
                        if (PublicUtil.isNotEmpty(list2)){
                            for (MenuVO l2 : list2) {
                                /** 只获取2级菜单*/
                                List<MenuVO> list3 = l2.getChildren();
                                for (MenuVO l3 : list3) {
                                    /** 封装数据返回给前端*/
                                    l3.setModuleId(l3.getId());
                                    l3.setChecked(false);
                                    l3.setId(null);
                                    if (PublicUtil.isNotEmpty(menuList)){
                                        for(SystemQuickModuleDO module : menuList){
                                            /** 判断如果moduleId 一样表示已经添加*/
                                            if (module.getModuleId().intValue() == l3.getModuleId().intValue()){
                                                l3.setChecked(true);
                                                l3.setId(module.getId());
                                                break;
                                            }
                                        }
                                        l3.setChildren(null);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }


    /**
     * 根据用户id获取已加入快捷面板的记录
     * @param userInfo： 用户信息
     * @param type : 1 - 我的工作台展示，2- 已添加的快捷功能
     * @return Result
     * @author WFZ
     * @date 2019/5/6 10:45
     */
    @Override
    public Result listAddedByUserId(TokenUser userInfo, Integer type) {
        /** 判断是否超级管理员*/
        List<SystemRoleDO> roleDOList = systemRoleMapper.listSystemRoleByUserId(userInfo.getId());
        Set<Integer> roles = new HashSet<>(4);
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(roleDOList)){
            /** 记录角色id*/
            for (SystemRoleDO role : roleDOList){
                roles.add(role.getId());
                if (role.getAdministrators().intValue()==1){
                    flag = true;
                }
            }
        }else {
            roles.add(0);
        }
        return Result.succeed(baseMapper.listAddByUserId(flag ? null:roles,userInfo.getId(),type));
    }


    /**
     * 新增
     *
     * @param model : SaAsQuickModule实体
     * @return Result
     * @author FZ
     * @date 2019/6/25 11:07
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result add(SystemQuickModuleDO model) {

        /** 插入之前查看该模块id是否存在*/
        List<MenuVO> menuVOS = systemMenuMapper.listMenuDetailsByMenuIds(Arrays.asList(model.getModuleId().toString()) , 0);
        if (PublicUtil.isNotEmpty(menuVOS)){
            /**
             * 验证用户是否已经添加过
             */
            int flag = getQuickModuleByUserIdAndModuleId(model.getUserId(),model.getModuleId());
            if (flag > 0 ){
                return Result.succeed();
            }
            /**
             * 插入数据 -- 插入时子查询最大排序值
             */
            baseMapper.insertSystemQuickModule(model);
            return Result.succeed();
        }
        return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
    }


    /**
     * 清空数据 （根据员工id）
     *
     * @param userInfo ： 用户信息
     * @return Result
     * @author WFZ
     * @date 2019/6/25 10:58
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result wipeData(TokenUser userInfo) {
        if (PublicUtil.isNotNull(userInfo.getId())){
            QueryWrapper<SystemQuickModuleDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.apply("user_id = " + userInfo.getId() );
            baseMapper.delete(queryWrapper);
            return Result.succeed();
        }
        return Result.failed(CodeEnum.USER_ABNORMAL_LOGIN_EXCEPTION);
    }


    /**
     * 批量排序
     *
     * @param vo : 排序请求类
     * @return Result
     * @author WFZ
     * @date 2019/5/22 14:23
     */
    @Override
    public Result batchSort(QuickModuleBatchSortDTO vo, TokenUser userInfo) {
        if (PublicUtil.isNotEmpty(vo.getList())){
            /** 数据校验*/
            List<QuickModuleSortDTO> list = vo.getList();
            Set<String> set = new HashSet<>();
            List<String> set1 = new ArrayList<>();
            list.forEach(key -> {
                if (PublicUtil.isNotEmpty(key.getId())){
                    set.add(key.getId());
                }
                if (PublicUtil.isNotEmpty(key.getSort())){
                    set1.add(key.getSort());
                }
            });
            if (set.size() != list.size()){
                return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"id不能为空且不能重复");
            }
            if (set1.size() != list.size()){
                return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"sort值不能为空");
            }

            /** 获取用户已经添加的快捷功能*/
            List<SystemQuickModuleDO> quickModuleList = getOpenQuickModule(userInfo.getId());
            if (PublicUtil.isNotEmpty(quickModuleList)){
                /** 判断传入的参数是否合法*/
                boolean flag = true;
                for (String dto : set){
                    for (SystemQuickModuleDO quick : quickModuleList){
                        if (dto.equals(quick.getId().toString())){
                            flag = false;
                        }
                    }
                    if (flag){
                        return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
                    }
                }
                baseMapper.updateBatch(list);
                return Result.succeed();
            }else {
                return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
            }

        }
        return Result.failed(CodeEnum.SELECT_IS_EMPTY);

    }

    /**
     * 批量新增
     *
     * @param list
     * @return Result
     * @author WFZ
     * @date 2019/6/25 10:09
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result batchInsert(TokenUser userInfo, List<BatchInsertDTO> list) {
        /**
         *  首先删除该员工下的所有快捷功能
         */
        Result result = wipeData(userInfo);
        if (PublicUtil.isResultSuccess(result) && PublicUtil.isNotEmpty(list)){
            Set<String> moduleIds = new HashSet<>();
            for (BatchInsertDTO dto : list){
                if (PublicUtil.isEmpty(dto.getModuleId())){
                    return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"模块不能为空");
                }
                if (PublicUtil.isEmpty(dto.getSort())){
                    return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"sort值不能为空");
                }
                moduleIds.add(dto.getModuleId());
            }
            if (moduleIds.size() != list.size()){
                return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),"模块不能重复");
            }
            List<MenuVO> menuVOList = systemMenuMapper.listMenuDetailsByMenuIds(new ArrayList<>(moduleIds), 0);
            if (PublicUtil.isEmpty(menuVOList) ||  menuVOList.size() != list.size()){
                return Result.failed(CodeEnum.ILLEGAL_DATA_ERROR);
            }
            /**
             *  批量插入
             */
            baseMapper.batchInsert(list,userInfo.getId());
            return Result.succeed();
        }
        return result;
    }


    /**
     * 根据用户id获取已开通的快捷功能
     * @author      WFZ
     * @param       userId : 员工id
     * @return      List<SystemQuickModuleDO>
     * @date        2019/5/21 11:30
     */
    public List<SystemQuickModuleDO> getOpenQuickModule(Serializable userId){
        QueryWrapper<SystemQuickModuleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("user_id = " + userId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据用户id、模块id判断是否已经开通
     * @author      WFZ
     * @param       userId : 用户id
     * @param       moduleId : 功能模块id
     * @return      int : 1-已经开通，0-未开通
     * @date        2019/5/21 11:30
     */
    public int getQuickModuleByUserIdAndModuleId(Serializable userId, Serializable moduleId){
        QueryWrapper<SystemQuickModuleDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("user_id = " + userId +" AND module_id = " +moduleId + " LIMIT 1");
        return baseMapper.selectCount(queryWrapper);
    }

}

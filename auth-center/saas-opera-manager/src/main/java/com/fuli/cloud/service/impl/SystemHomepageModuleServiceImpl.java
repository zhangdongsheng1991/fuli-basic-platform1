package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.SystemHomepageModuleMapper;
import com.fuli.cloud.mapper.SystemModuleMapper;
import com.fuli.cloud.mapper.SystemRoleHomepageModuleMapper;
import com.fuli.cloud.model.SystemHomepageModuleDO;
import com.fuli.cloud.model.SystemRoleHomepageModuleDO;
import com.fuli.cloud.service.SystemHomepageModuleService;
import com.fuli.cloud.vo.HomePageModuleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author XQ
 * @date 2019/8/1 15:36
 */
@Service
public class SystemHomepageModuleServiceImpl extends ServiceImpl<SystemHomepageModuleMapper, SystemHomepageModuleDO> implements SystemHomepageModuleService {

    @Resource
    SystemModuleMapper systemModuleMapper;

    @Resource
    SystemHomepageModuleMapper systemHomepageModuleMapper;

    @Resource
    SystemRoleHomepageModuleMapper systemRoleHomepageModuleMapper;

    /**
     * 功能描述: 根据角色查询首页工作台模块<br>
     * 〈0 查看（展示所有已选择的）  1 编辑（展示所有已选择和未选中的）  2 新增（展示所有  不选中）〉
     * @param moduleId 模块ID 角色id
     * @param roleId 角色id
     * @param type 类型
     * @return  List<HomePageModuleVo>
     * @author XQ
     * @date 2019/8/1 10:34
     */
    @Override
    public List<HomePageModuleVo> getHomepageModuleList(Integer moduleId , Integer roleId , Integer type){
        //选中的模块
        Set<Integer> checkdHomepageModuleIds = new HashSet<>();
        Map<Integer , Integer> maps = new HashMap<>();
        if(roleId != null){
            // 角色工作台关联
            List<SystemRoleHomepageModuleDO> systemRoleHomeList =  systemRoleHomepageModuleMapper.selectList(new QueryWrapper<SystemRoleHomepageModuleDO>().eq("role_id" , roleId));
            if(systemRoleHomeList == null || systemRoleHomeList.size() < 1){
                throw new CustomException(CodeEnum.HOME_PAGE_ID_ERROR);
            }
            //选中的菜单
            systemRoleHomeList.forEach(systemRoleHomepageModuleDO -> {
                checkdHomepageModuleIds.add(systemRoleHomepageModuleDO.getHomepageModuleId());
                maps.put(systemRoleHomepageModuleDO.getHomepageModuleId() , systemRoleHomepageModuleDO.getHomepageModuleId());
            });

        }
        /*返回的工作台模块*/
        List<HomePageModuleVo> homePageList = new ArrayList<>();
        QueryWrapper<SystemHomepageModuleDO> queryWrapper = new QueryWrapper<>();

        Set<Integer> queryList = null;
        // 查看
        if(type.intValue() == 1){
            queryList = checkdHomepageModuleIds;
            queryWrapper.in(!CollectionUtils.isEmpty(queryList) , "id" , queryList);
        }
        queryWrapper.ne("is_default" , 0);
        queryWrapper.notLike("name" ,"审批");
        if(moduleId != null){
            queryWrapper.eq("module_id" ,moduleId);
        }

        // 工作台模块
        List<SystemHomepageModuleDO> homePage = systemHomepageModuleMapper.selectList(queryWrapper);
        homePage.forEach(systemHomepageModuleDO ->{
            HomePageModuleVo homePageModuleVo = new HomePageModuleVo();
            BeanUtils.copyProperties(systemHomepageModuleDO ,homePageModuleVo);
            //查看
            if (type.intValue() == 0){
                homePageModuleVo.setChecked(true);
                //编辑
            }else if (type.intValue() == 1){
                if(homePageModuleVo.getId() == maps.get(homePageModuleVo.getId())){
                    homePageModuleVo.setChecked(true);
                }else {
                    homePageModuleVo.setChecked(false);
                }
            }else{
                homePageModuleVo.setChecked(false);
            }
            homePageList.add(homePageModuleVo);
        });
        return homePageList;
    }

}

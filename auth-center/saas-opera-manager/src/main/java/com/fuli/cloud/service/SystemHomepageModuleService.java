package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.model.SystemHomepageModuleDO;
import com.fuli.cloud.vo.HomePageModuleVo;

import java.util.List;

/**
 * @author XQ
 * @date 2019/8/1 15:36
 */
public interface SystemHomepageModuleService extends IService<SystemHomepageModuleDO> {

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
    List<HomePageModuleVo> getHomepageModuleList(Integer moduleId , Integer roleId , Integer type);

}

package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.quick.BatchInsertDTO;
import com.fuli.cloud.dto.quick.QuickModuleSortDTO;
import com.fuli.cloud.model.SystemQuickModuleDO;
import com.fuli.cloud.vo.menu.MenuButtonVO;
import com.fuli.cloud.vo.menu.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *  工作台快捷功能表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SystemQuickModuleMapper extends BaseMapper<SystemQuickModuleDO> {

    /**
     * 根据用户id查询已添加的快捷功能列表
     * @author      WFZ
     * @param 	    roles : 角色
     * @param 	    userId : 员工id
     * @param 	    type : 1 - 我的工作台展示，2- 已添加的快捷功能
     * @return      List<Integer>
     * @date        2019/6/25 14:29
     */
    List<MenuButtonVO> listAddByUserId(@Param("roles") Set<Integer> roles, @Param("userId") Serializable userId, @Param("type")Integer type);


    /**
     * 批量更新
     * @author      WFZ
     * @param 	    list: 集合
     * @return      int ：影响的行数
     * @date        2019/6/25 17:08
     */
    int updateBatch(List<QuickModuleSortDTO> list);

    /**
     * 插入数据 - 内部查询最大sort 值
     * @author      WFZ
     * @param 	    module ：SystemQuickModuleDO实体类
     * @return      int ： 影响的行数
     * @date        2019/6/25 19:52
     */
    int insertSystemQuickModule(SystemQuickModuleDO module);

    /**
     * 批量插入
     * @author      WFZ
     * @param 	    list: 集合
     * @param 	    userId: 用户id
     * @return      int ：影响的行数
     * @date        2019/6/25 17:08
     */
    int batchInsert(List<BatchInsertDTO> list, Serializable userId);
}

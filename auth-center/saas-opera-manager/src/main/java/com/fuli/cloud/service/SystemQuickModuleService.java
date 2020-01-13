package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.quick.BatchInsertDTO;
import com.fuli.cloud.dto.quick.QuickModuleBatchSortDTO;
import com.fuli.cloud.model.SystemQuickModuleDO;
import com.fuli.cloud.model.TokenUser;

import java.util.List;

/**
 * @Description:    快捷面板接口
 * @Author:         FZ
 * @CreateDate:     2019/4/26 17:10
 * @Version:        1.0
 */
public interface SystemQuickModuleService extends IService<SystemQuickModuleDO> {

    /**
     *  查询数据列表
     * @author      FZ
     * @param 	    userInfo： 用户信息
     * @return      PageResult
     * @date        2019/6/25 11:07
     */
    Result list(TokenUser userInfo);

    /**
     * 根据用户id获取已加入快捷面板的记录
     * @author      WFZ
     * @param 	    userInfo： 用户信息
     * @param 	    type : 1 - 我的工作台展示，2- 已添加的快捷功能
     * @return      Result
     * @date        2019/6/25 11:07
     */
    Result listAddedByUserId(TokenUser userInfo, Integer type);

    /**
     *  新增
     * @author      FZ
     * @param 	    model : SaAsQuickModule实体
     * @return      Result
     * @date        2019/6/25 11:07
     */
    Result add(SystemQuickModuleDO model);


    /**
     * 清空数据 （根据员工id）
     * @author      WFZ
     * @param 	    userInfo： 用户信息
     * @return      Result
     * @date        2019/6/25 10:58
     */
    Result wipeData(TokenUser userInfo);

    /**
     * 批量排序
     * @author      WFZ
     * @param 	    vo : 排序请求类
     * @param 	    userInfo： 用户信息
     * @return      Result
     * @date        2019/6/25 11:07
     */
    Result batchSort(QuickModuleBatchSortDTO vo, TokenUser userInfo);

    /**
     * 批量新增
     * @author      WFZ
     * @param 	    list
     * @param 	    userInfo： 用户信息
     * @return      Result
     * @date        2019/6/25 10:09
     */
    Result batchInsert(TokenUser userInfo, List<BatchInsertDTO> list);

}

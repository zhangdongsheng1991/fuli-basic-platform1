package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.homepage.HomepageUserDTO;
import com.fuli.cloud.model.TokenUser;

import java.util.List;

/**
 * @Description:    首页模块管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 11:16
 * @Version:        1.0
*/
public interface SystemHomepageUserService {

    /**
     * 获取拥有的首页模块列表
     * @author      WFZ
     * @param 	    userInfo ： 登录用户信息
     * @return      Result
     * @date        2019/6/26 11:16
     */
    Result list(TokenUser userInfo);

    /**
     * 批量新增
     * @author      WFZ
     * @param 	    list
     * @param 	    userInfo： 登录用户信息
     * @return      Result
     * @date        2019/6/25 10:09
     */
    Result batchInsert(TokenUser userInfo, List<HomepageUserDTO> list);

    /**
     * 重置（清空用户已加入排序的模块）
     * @author      WFZ
     * @param 	    userInfo： 登录用户信息
     * @return      Result
     * @date        2019/6/26 15:10
     */
    Result reset(TokenUser userInfo);

}

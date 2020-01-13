package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.model.SystemMenuDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.vo.MenuTreeVO;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * @Description:    菜单接口
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 10:37
 * @Version:        1.0
*/
public interface MenuService {

    /**
     * 根据用户id获取菜单，只获取菜单不获取按钮
     * @author      WFZ
     * @param 	    user : 登录用户
     * @return      Result
     * @date        2019/6/26 10:29
     */
    Result listMenuByUserId(TokenUser user);

    /**
     * 获取渠道运营菜单
     * @author      WFZ
     * @param 	    user : 登录用户
     * @return      Result
     * @date        2019/6/26 10:29
     */
    Result placeMenu(TokenUser user);


    /**
     * 根据菜单id获取拥有的当前页面的按钮
     * @author      WFZ
     * @param 	    menuId
     * @param 	    user : 登录用户
     * @return      Result
     * @date        2019/7/31 14:53
     */
    Result listButtonByMenuId(Serializable menuId,TokenUser user);

    /**
     * 功能描述: 查询出所有菜单<br>
     * 〈0 查看  1 编辑  2 新增〉
     * @param moduleId 模块id
     * @param roleId
     * @param type
     * @return  List
     * @author XQ
     * @date 2019/8/1 10:00
     */
    List<MenuTreeVO> getAllMenuTree(Integer moduleId , Integer roleId , Integer type);


}

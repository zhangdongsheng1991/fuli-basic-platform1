package com.fuli.cloud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemMenuDO;
import com.fuli.cloud.vo.menu.MenuButtonVO;
import com.fuli.cloud.vo.menu.MenuVO;
import com.fuli.cloud.vo.SystemModuleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 *  用户表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SystemMenuMapper extends BaseMapper<SystemMenuDO> {

    /**
     * 根据用户角色获取拥有的菜单模块
     * @author      WFZ
     * @param 	    roles :  角色id集合
     * @param 	    type :  区分是否要查询渠道门户模块
     * @return      Result
     * @date        2019/7/29 17:33
     */
    List<SystemModuleVO> listSystemModuleByRoles(@Param("roles") Set<Integer> roles , Integer type);

    /**
     * 根据角色获取菜单，只获取菜单不获取按钮
     * @author      WFZ
     * @param 	    roles :  角色id集合
     * @param 	    type :  为null 表示按钮也查询
     * @return      Result
     * @date        2019/6/26 10:29
     */
    List<MenuVO> listMenuByRoles(@Param("roles") Set<Integer> roles, Integer type);

    /**
     * 根据菜单Id返回菜单相关信息(集合)
     * @author      WFZ
     * @param 	    list :  菜单id集合
     * @param 	    type :  菜单或者按钮
     * @return      Result
     * @date        2019/6/26 10:29
     */
    List<MenuVO> listMenuDetailsByMenuIds(@Param("list")List<String> list,Integer type);

    /**
     * 根据菜单id获取拥有的当前页面的按钮
     * @author      WFZ
     * @param       menuId : 菜单id
     * @param       roles :  角色id集合
     * @return      Result
     * @date        2019/7/31 15:15
     */
    List<MenuButtonVO> listButtonByMenuId(Serializable menuId , @Param("roles") Set<Integer> roles);

    /**
     * 根据URL获取菜单
     * @author      WFZ
     * @param 	    url
     * @return      Result
     * @date        2019/7/31 19:18
     */
    List<SystemMenuDO> findByUrl(@Param("url") String url);
}

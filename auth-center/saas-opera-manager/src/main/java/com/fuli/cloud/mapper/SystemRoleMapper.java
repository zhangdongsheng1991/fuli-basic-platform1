package com.fuli.cloud.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.vo.RoleIdNameVo;
import com.fuli.cloud.vo.RoleNameVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  角色表
 * @author WFZ 2019-07-29
 */
@Mapper
@Repository
public interface SystemRoleMapper extends BaseMapper<SystemRoleDO> {

    /**
     * 根据用户获取拥有的角色列表
     * @author      WFZ
     * @param 	    userId : 用户id
     * @return      Result
     * @date        2019/7/29 17:59
     */
    List<SystemRoleDO> listSystemRoleByUserId(@Param("userId") Serializable userId);

    @Select("SELECT " +
            "  r.id, " +
            "  r.`name` " +
            "FROM " +
            "  system_role r " +
            "LEFT JOIN system_role_user ru ON r.id = ru.role_id " +
            "WHERE " +
            "  r.`status` = 1 " +
            "AND " +
            "  ru.user_id = #{id} ")
    List<RoleIdNameVo> getRoleNamesByUserId(Integer id);


    /**
     * <pre>
     * Description: 返回所有启用的角色的id和name
     * </pre>
     *
     * @return 返回所有启用的角色的id和name
     * @author chenyi
     * @date 15:30 2019/7/31
     **/
    @Select("SELECT " +
            " r.id, " +
            " r.`name` " +
            "FROM " +
            " system_role AS r " +
            "WHERE " +
            " r.`status` = 1 " +
            "and r.administrators = 0")
    List<RoleIdNameVo> listRoleIdNameEnable();


    /**
     * <pre>
     * Description: 根据用户id查询用户的角色名称
     * </pre>
     *
     * @param userIdsStr 字符串 多个用户id，逗号分隔
     * @return 用户id和角色名称的List
     * @author chenyi
     * @date 17:47 2019/8/5
     **/
    @Select("SELECT " +
            "  r.`name` roleName, " +
            "  ru.user_id " +
            "FROM " +
            "  system_role AS r " +
            "LEFT JOIN system_role_user AS ru ON r.id = ru.role_id " +
            "WHERE " +
            " r.`status` = 1 " +
            " AND  ru.user_id IN (${userIdsStr})")
    List<RoleNameVo> getRoleNameUserIdList(@Param("userIdsStr") String userIdsStr);


    /**
     * 获取所有的菜单, url不为空
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/10 16:50
     */
    List<Map<String,String>> queryAllMenu();

    /**
     * 获取所有的权限菜单
     * @author      WFZ
     * @param       roleId
     * @return      Result
     * @date        2019/9/10 16:50
     */
    List<Map<String,String>> queryAllRoleMenu(@Param("roleId") Integer roleId);

    /**
     * 获取所有用户的角色
     * @author      WFZ
     * @param
     * @return      Result
     * @date        2019/9/10 16:50
     */
    List<Map<String,String>> queryAllUserRole();

}

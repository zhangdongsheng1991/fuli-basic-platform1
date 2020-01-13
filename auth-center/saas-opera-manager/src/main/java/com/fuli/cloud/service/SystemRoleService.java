package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.vo.RoleIdNameVo;
import com.fuli.cloud.vo.RoleNameVo;
import com.fuli.cloud.vo.SystemRoleDetailsVo;
import com.fuli.cloud.vo.SystemRoleListVO;
import com.fuli.cloud.vo.menu.AuthModuleVO;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author admin3
 */
public interface SystemRoleService extends IService<SystemRoleDO> {

    /**
     * 查询角色列表
     * @param querySystemRoleDTO 分页条件
     * @return 分页后查询的结果集
     */
    Result<PageResult<SystemRoleListVO>> getSystemRoleListPage(QuerySystemRoleDTO querySystemRoleDTO);

    /**
     * 查询角色详情查询
     * @param querySystemRoleInfoDTO 详情
     * @return 查询结果
     */
    Result<SystemRoleDetailsVo> getSystemRoleInfo(QuerySystemRoleInfoDTO querySystemRoleInfoDTO);

    /**
     * 新增角色
     * @param addSystemRoleDTO 新增角色
     * @param user
     * @return 新增结果
     */
    Result saveSystemRole(AddSystemRoleDTO addSystemRoleDTO , TokenUser user);

    /**
     * 修改角色
     * @param updateSystemRoleDTO 修改角色
     * @param user
     * @return 修改jieguo
     */
    Result updateSystemRole(UpdateSystemRoleDTO updateSystemRoleDTO , TokenUser user);

    /**
     * 注销角色
     * @param logoutSystemRoleDTO 注销角色
     * @param  user
     * @return 注销角色
     */
    Result logoutSystemRole(LogoutSystemRoleDTO logoutSystemRoleDTO , TokenUser user);

    /**
     * 功能描述: 查询出所有模块<br>
     * 〈0 查看  1 编辑  2 新增〉
     * @return 需要展示的工作台模块以及模块下对应的菜单
     * @author XQ
     * @date 2019/8/1 10:11
     */
    List<AuthModuleVO> getAllModule(Integer roleId , Integer type);

    /**
     * 查询模块下的菜单以及工作台模块
     */
    List<AuthModuleVO> getAuthModule(Integer roleId, boolean administrators, Integer onlyChecked);

    /**
     * <pre>
     * Description: 根据用户id保存或更新用户的角色
     * </pre>
     *
     * @param userId  用户id
     * @param roleIds 角色id集合
     * @author chenyi
     * @date 16:21 2019/7/30
     **/
    void saveOrUpdateRoleUserByUserId(Integer userId, @Nullable Set<Integer> roleIds);

    /**
     * <pre>
     * Description: 查询所有启动的角色的id和name ，除了超级管理员
     * </pre>
     *
     * @author chenyi
     * @return List<RoleIdNameVo>
     * @date 15:21 2019/7/31
     **/
    List<RoleIdNameVo> listRoleIdNameEnable();

    /**
     * 判断用户是否超级管理员
     * @author      WFZ
     * @param 	    userId : 用户id
     * @return      Boolean
     * @date        2019/7/31 17:02
     */
    Boolean isAdministratorByUserId(Serializable userId);


    /**
     * <pre>
     * Description: 根据用户id查询用户的角色名称
     * </pre>
     *
     * @param userIdsStr 字符串 多个用户id，逗号分隔
     * @return 用户id和角色名称的List
     * @author chenyi
     * @date 17:44 2019/8/5
     **/
    List<RoleNameVo> getRoleNameUserIdList(String userIdsStr);
}

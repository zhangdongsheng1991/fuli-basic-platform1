package com.fuli.server.constants;

/**
 * @Description:    url长量记录
 * @Author:         WFZ
 * @CreateDate:     2019/5/28 11:12
 * @Version:        1.0
*/
public class GatewayCommonConstant {

    /**
     * 客户端模式
     */
    public static final String CLIENT_CREDENTIALS="client_credentials";
    /**
     * 密码模式
     */
    public static final String PASSWORD="password";

    /**
     * SaaS门户
     */
    public static final String saAsPORTAL="saasPortal";
    /**
     * SaaS运营
     */
    public static final String saAsOperation="saasOperation";


    /**
     * SaaS门户菜单redis
     */
    public static final String PORTAL_MENU="saAsPortalMenu";
    /**
     * SaaS门户管理员角色redis
     */
    public static final String PORTAL_ADMIN_ROLE="saAsPortalAdminRole:";
    /**
     * SaaS门户用户角色redis
     */
    public static final String PORTAL_USER_ROLE="saAsPortalUserRole:";
    /**
     * SaaS门户角色菜单redis
     */
    public static final String PORTAL_ROLE_MENU="saAsPortalRoleMenu:";


    /**
     * SaaS运营菜单redis
     */
    public static final String OPERATION_MENU="saAsOperationMenu";
    /**
     * SaaS运营用户角色redis
     */
    public static final String OPERATION_USER_ROLE="saAsOperationUserRole:";
    /**
     * SaaS运营角色菜单redis
     */
    public static final String OPERATION_ROLE_MENU="saAsOperationRoleMenu:";


    public static final String URL="url";
}

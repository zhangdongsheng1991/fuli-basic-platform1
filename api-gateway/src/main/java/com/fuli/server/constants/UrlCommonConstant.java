package com.fuli.server.constants;

/**
 * @Description:    url长量记录
 * @Author:         WFZ
 * @CreateDate:     2019/5/28 11:12
 * @Version:        1.0
*/
public class UrlCommonConstant {

    /**
     * 切换企业
     */
    public static final String CUT_TOKEN="/api-auth/cut/token";

    /**
     * APP退出
     */
    public static final String APP_LOGOUT="/api-user/appUser/logout,/user-center/appUser/logout";


    /**
     * 中台不需要处理的URL
     */
    public static final String MID_WITH="/api-middle/api-platform/createTokenForPlatform";
}

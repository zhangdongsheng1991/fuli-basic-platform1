package com.fuli.user.constant;

/**
 * 中台Api接口地址
 *
 * @author pcg
 * @version 1.0
 * @date 2019-4-17 14:01:54
 */
public class PlatformApiUriConstant {

    /**
     * 获取accessToken接口
     */
    public final static String GET_ACCESS_TOKEN = "/cgi-bin/oauth2-platform/access_token?grant_type=client_credentials";

    /**
     * 统一账号密码多业务线登录
     */
    public final static String THIRD_LOGIN = "/cgi-bin/oauth2-platform/third/login";

    /**
     * 获取中台实名认证接口
     */
    public final static String FEAL_URI = "/cgi-bin/eid/two/verify";

    /**
     * 新用户同步到中台
     */
    public final static String REGISTER = "/cgi-bin/api-platform/register/register_user";

    /**
     * 推送用户ID到中台接口
     */
    public final static String REGISTER_USER_ID = "/cgi-bin/api-platform/register/register_userId";

    /**
     * 更新/删除用户信息
     */
    public final static String MODIFY_USER = "/cgi-bin/api-platform/modify/modifyUser";

    /**
     * 平台密码正确性接口  非本地注册用户在我们系统修改密码 需要调用中台接口线判断源密码的正确性
     * 判断通过后再调用账户密码同步接口（中台会判断这个用户是我们本地注册的还是其他业务线的）
     *
     */
    public final static String CHECK_PWD = "/cgi-bin/oauth2-platform/chkPwd";

    /**
     * 账户密码同步接口
     * 如果是 本地注册用户修改密码同步到中台 中台跟着修改 从而保证中台存的密码和我们本地库一致同步
     * 如果是 非本地注册用户修改密码  那么中台改完后 会通知 用户源注册系统修改同步 从而保持账户密码一致统一
     */
    public final static String REGISTER_LOGIN ="/cgi-bin/oauth2-platform/register_login";

}

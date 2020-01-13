package com.fuli.cloud.constant;



/**
 * @Description:    公共常量
 * @Author:         FZ
 * @CreateDate:     2019/4/18 12:37
 * @Version:        1.0
*/
public class CommonConstant {

    /** 成功*/
    public static final int SUCCESS = 0;
    /** 密码登录*/
    public static final String LOGIN_OPERATE = "1";
    /** 验证码登录*/
    public static final String LOGIN_APP_SYSTEM = "2";

    /** 头部封装用户信息 KEY*/
    public static final String HEAD_USER_INFO_KEY= "x-client-token-user";

    /** 用户初始密码*/
    public static final String USER_INIT_PASSWORD = "b94d8169a391034a34351d7a1e63c4a8";
    /** 管理员初始密码*/
    public static final String ADMIN_INIT_PASSWORD = "cc209a064762d672c7cc2ef10c45372f";

    /** 密码截取字符串*/
    public static final String PASSWORD_SPLIT_SIGN = "##&&&";

    /** 用户权限菜单Redis Key*/
    public static final String MENU_REDIS_KEY = "operationMenu:|";

    /** 截取字符串长度*/
    public static int SPLIT_LENGTH = 10;

    /** clientId*/
    public static final String CLIENT_ID = "saasOperation";
    /** clientSecret*/
    public static final String CLIENT_SECRET = "SaaSOperation2019";
    /** grantType*/
    public static final String CLIENT_GRANT_TYPE = "password";

    /**
     * 下级参数列表
     */
    public final static String CHILDREN = "children";

    /**
     * The constant Y.
     */
    public static final Integer Y = 1;
    /**
     * The constant N.
     */
    public static final Integer N = 0;

    /**
     * 状态：启用
     */
    public static final byte STATUS_ENABLE = 1;

    /**
     * 状态：禁用
     */
    public static final byte STATUS_DISABLE = 0;


	/** 公共跳转地址*/
    public static final String KEY_LIST = "/list";
    public static final String KEY_ADD = "/add";
    public static final String KEY_UPDATE = "/update";
    public static final String KEY_DELETE = "/delete";
    public static final String KEY_BATCH_REMOVE = "/batchRemove";
    public static final String KEY_BATCH_INSERT = "/batchInsert";
    public static final String KEY_BATCH_CHANGE = "/batchChange";
	public static final String APP_PUSH_MESSAGE = "app_push_message";
	public static final String STR_RESULT= "result";
	public static final String STR_ID= "id";

}

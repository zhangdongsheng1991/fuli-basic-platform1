package com.fuli.user.constant;



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
    public static final int LOGIN_PASSWORD = 1;
    /** 验证码登录*/
    public static final int LOGIN_CODE = 2;


    /** 用户初始密码*/
    public static final String USER_INIT_PASSWORD = "b94d8169a391034a34351d7a1e63c4a8";
    /** 管理员初始密码*/
    public static final String ADMIN_INIT_PASSWORD = "cc209a064762d672c7cc2ef10c45372f";

    /** 验证码登录*/
    public static final int PASSWORD_ERROR_COUNT = 5;

    /** 设备号保存在redis的目录名称*/
    public static final String FULI_DEVICE_NUM = "fuLiDeviceNum:";

    /** 用户密码连续输入错误5次保存在redis的目录名称*/
    public static final String APP_USER_PASSWORD = "fuLiErrorPwdCount:";

    /** 支付密码连续输入错误5次保存在redis的目录名称*/
    public static final String APP_PLAY_PASSWD = "APPPLAYPASSWD";


    /** token保存到redis的有效期 (暂时为1天)*/
    public static final long TOKEN_USEFUL_LIFE = 86400;
    
    /** 密码截取*/
    public static final String PASSWORD_SPLIT_SIGN = "##&&&";

    /** 发送短信时区分*/
    public static final String SMS_SEND_SOURCE_LOGIN = "login";
    public static final String SMS_SEND_SOURCE_FINDPWD = "findpwd";
    public static final String SMS_SEND_SOURCE_REGISTER = "register";
    public static final String SMS_SEND_SOURCE_BINDING = "binding";
    public static final String SMS_SEND_SOURCE_UPDATEPWD = "updatepwd";
    public static final String SMS_SEND_SOURCE_UPDATE_PAYPWD = "updatepaypwd";
    public static final String SMS_SEND_SOURCE_FORGET_PWD = "forgetPwd";
    public static final String SMS_UPDATE_PHONE = "updatePhone";
    public static final String X_CLIENT_TOKEN_USER = "x-client-token-user";

    /** 头部封装用户信息 KEY*/
    public static final String HEAD_USER_INFO_KEY= "x-client-token-user";

    /**
     * 在中台的标记
     */
    public final static String FU_LI_LABEL = "FLJR";


    /** clientId*/
    public static final String CLIENT_APP = "app";

    /** clientId*/
    public static final String CLIENT_ID = "shuKeApp";
    /** clientSecret*/
    public static final String CLIENT_SECRET = "shuKeApp2019";
    /** grantType*/
    public static final String CLIENT_GRANT_TYPE = "password";

    /** 切换企业 客户端key*/
    public static final String CLIENT_SECRET_SWITCHOVER = "switchover";

    /** SaaS门户 clientId*/
    public static final String CLIENT_ID_PORTAL = "saasPortal";
    /** SaaS门户 clientId*/
    public static final String CLIENT_SECRET_PORTAL = "saasPortal2019";

    /** redis存储中台用户id名称*/
    public static final String MIDDLE_USER_ID = "middle_user_id:";
}

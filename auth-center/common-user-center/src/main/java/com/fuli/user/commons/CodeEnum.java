package com.fuli.user.commons;

public enum CodeEnum {

	SUCCESS(0, "操作成功"),
	ERROR(10208000, "操作失败"),
	ILLEGAL_DATA_ERROR(10208001, "操作失败,传入非法参数"),
	PARAM_ERROR(10208002,"请求参数错误"),
	SQL_SYNTAX_EXCEPTION(10208003, "SQL异常"),
	GLOBAL_EXCEPTION(10208004, "系统忙，请稍后再试或联系管理员"),
	REQUEST_GET_SUPPORTED(10208005, "请求不支持GET，请使用POST"),
	REQUEST_POST_SUPPORTED(10208006, "请求不支持POST，请使用GET"),

	INVOKE_MIDDLE_SERVICE_ERROR(10208007, "调用中台服务出错，请稍后再试"),
	REQUEST_PARAMETER_EXCEPTION(10208008, "缺少请求参数错误"),
	// 请求方法错误
	HTTP_REQUEST_METHOD_EXCEPTION(10208009, "请求方法错误"),
	TYPE_CONVERT_ERROR(10208010,"参数类型转换错误"),

	/** 内部异常 */
	ERROR_CODE_500(10208011, "内部异常"),


	/**参数输入不合法！*/
	SYSTEM_SERVER_20204001(20204001, "参数输入不合法"),

	USER_NOT_FOUND(20204002, "您输入的手机号/用户名不存在，请重新输入"),
	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	USERNAME_NOT_NULL(1003, "用户名不能为空"),
	/**
	 * @Description:(用户登录注册验证)
	 * @author      fengjing
	 * @date        2019/5/23 19:15
	 */
	CONFIRM_PASSWORD_NOT_NULL(1033, "确认密码不能为空"),
	/**
	 * @Description:(您输入的登录密码错误)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	LOGON_PWD_ERROR(1034, "您输入的登录密码错误"),
	/**
	 * @Description:(您输入的手机号已经注册了，请使用其他手机号)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	PHONE_ALREADY_REGISTER(1035, "您输入的手机号已经注册了，请使用其他手机号"),
	/**
	 * @Description:(修改成功，请使用新手机号码登录)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	PHONE_UPDATE_SUCCESS(1036, "修改成功，请使用新手机号码登录"),
	/**
	 * @Description:(修改失败，请重试)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	PHONE_UPDATE_FAILED(1037, "修改失败，请重试"),
	/**
	 * @Description:(您的身份证信息已实名认证)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	IDENTITY_INFORMATION_REAL_NAME(1038, "您的身份证信息已实名认证"),

	/**
	 * @Description:(姓名和身份证信息不匹配)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	NAME_IDENTITY_MISMATCH(1043, "姓名与身份证不匹配"),

	/**
	 * @Description:(saas登录手机号或者用户名不能为空)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	PHONE_PWD_NOT_NULL(1039, "手机号或者用户名不能为空"),
	/**
	 * @Description:(saas登录该账户不存在)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	NO_ACCOUNT_EXISTS(1040, "账号不存在"),
	/**
	 * @Description:(saas登录密码错误)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	PASSWORD_ERROR(1041, "密码错误"),
	/**
	 * @Description:(saas登录账号已经被注销)
	 * @author      fengjing
	 * @date        2019/6/11 14:29
	 */
	ACCOUNT_NUMBER_WRITE_OFF(1042, "账号已注销"),
	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	PASSWORD_NOT_NULL(1004, "密码不能为空"),

	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	PASSWORD_CODE_ERROR(1005, "您输入的帐号或密码错误，请重新输入"),
	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	VERIFY_CODE_NOT_NULL(1006, "验证码不能为空"),
	/**
	 * 用户忘记密码时，验证手机号
	 *
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	USER_NOT_FOUND_ERROR(1008, "您输入的手机号不存在，请重新输入"),

	/**
	 * 用户登录注册验证
	 *
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	CHECKED_USER_NONEXISTENT(1007, "用户不存在"),

	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	USER_NOT_EMPTY(1009, "您输入的手机号码已存在"),
	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/15 19:06
	 */
	GET_ACCESS_TOKEN_FAIL(1010, "获取token失败"),
	/**
	 * 登录用户与当前输入的手机号不匹配
	 * @author WFZ
	 * @date 2019/5/9 10:13
	 */
	LOGIN_USER_PHONE_NOT_MASTER(1024, "当前登录用户手机号与获取验证码手机号不匹配"),
	/**
	 * 用户登录注册验证
	 * 
	 * @author FZ
	 * @date 2019/4/18 19:06
	 */
	ACCESS_TOKEN_NULLITY(1011, "无效token"),

	/**
	 * 手机号码格式错误
	 *
	 * @author xq
	 * @date 2019/4/18 19:06
	 */
	PHONE_FORMAT_ERROR(1022, "您输入的手机号码格式有误"),

	/**
	 * 密码确认密码校验
	 * 
	 * @author xq
	 * @date 2019/4/18 19:06
	 */
	USER_ANGIN_PASSWORD(1012, "两次输入的密码不一致"),

	/**
	 * 修改密码时验证原密码
	 */
	OLDNEW_PWD_ATYPISM(1030,"原密码输入错误"),

	/**
	 * 修改密码时验证
	 */
	PLATFORM_OLDNEW_PWD_ATYPISM(1031,"原密码不能为空"),


	/**
	 * 验证码不能为空
	 * 
	 * @author FZ
	 * @date 2019/4/24 19:37
	 */
	VERIFY_CODE_EMPTY(1014, "验证码不能为空"),

	/**
	 * 手机号校验
	 *
	 * @author xq
	 * @date 2019/4/18 19:06
	 */
	USER_NOT_MOBLIE_EMPTY(1015, "手机号码不能为空"),

	/**
	 * 模式有误
	 * @author WFZ
	 * @date 2019/4/24 15:13
	 */
	LOGIN_TYPE_ERROR(1020, "登录模式有误"),


	USER_INIT_PASSWORD_UPDATE(30208111, "需修改密码"),


	/**
	 * saas登录,账号不存在
	 */
	ACCOUNT_NO_EXISTS(40208001, "账号不存在");

	private int code;
	private String msg;

	CodeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

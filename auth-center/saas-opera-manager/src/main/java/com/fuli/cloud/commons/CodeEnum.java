package com.fuli.cloud.commons;

/**
 * @Description:    响应枚举类
 * @Author:         WFZ
 * @CreateDate:     2019/5/14 11:50
 * @Version:        1.0
*/
public enum CodeEnum {

	/**
	 * 	 WFZ -- 请各位尽量按规格编码
	 * 	 编码 -- 具体服务
	 * 	 01    consumer-message-queue ; fuli-cloud-app-message ; provider-message-service-impl
	 * 	 02    ewallet-server
	 * 	 03    oss-server
	 * 	 04    saas-server
	 * 	 05    sms-server
	 * 	 06    system-server
	 * 	 07    upush-message
	 * 	 08    user-center
	 * 	 09    app-center
	 * 	 10    auth-server
	 *	 11    saas-system
	 *   12    api-gateway
	 *   13    basic-server
	 *
	 * 	 编码格式： 个数 8位
	 * 	 第1位：  错误码级别
	 * 	 第2-3位：区分业务线， 01-数据交换线，02-渠道业务线
	 * 	 第4-5位：区分服务， 如上
	 *	 第6-8位：序号--从001开始
	 *
	 *	 错误码级别
	 *	 1：代表系统错误，如数据库异常、服务调用异常
	 *	 2：代表参数错误，如手机号错误，身份证格式错误
	 *	 3：代表业务异常，比如余额不足，库存不足等
	 *	 4：代表权限异常，如没有调用权限等
	 */


    SUCCESS(0, "操作成功"),
	ERROR(10211000, "操作失败"),

	/**
	 * @author WFZ  HttpRequestMethodNotSupportedException
	 */
	HTTP_REQUEST_METHOD_EXCEPTION(10211001, "请求方法错误"),

	/**
	 * @author WFZ  MissingServletRequestParameterException
	 */
	REQUEST_PARAMETER_EXCEPTION(10211002, "缺少请求参数错误"),

	/**
	 * @author WFZ  SQLSyntaxErrorException  SQL异常
	 */
	SQL_SYNTAX_EXCEPTION(10211003, "SQL异常"),

	/**
	 * @author WFZ  全局EXCEPTION
	 */
	GLOBAL_EXCEPTION(10211004, "系统忙，请稍后再试或联系管理员"),
	USER_ABNORMAL_LOGIN_EXCEPTION(10211005,"用户登录状态异常，请重新登录"),
	PARAM_ERROR(10211006,"请求参数错误"),
	/**
	 * @author WFZ
	 */
	ILLEGAL_DATA_ERROR(10211007, "操作失败,传入非法参数"),
	TYPE_CONVERT_ERROR(10211008,"参数类型转换错误"),

	EXCEPTION(10211009, "接口请求异常"),

	/** 内部异常 */
	ERROR_CODE_500(10211010, "内部异常"),


	/**参数输入不合法！*/
	SYSTEM_SERVER_2020060001(20211001, "参数输入不合法"),
	/**内部异常*/
	SYSTEM_SERVER_2020060002(20211002, "内部异常"),

	ILLEGAL_ACCESS_TOKEN(20211003,"非法的token"),

	PHONE_EMPTY_EXCEPTION(20211007,"您输入的手机号码不存在，请重新输入"),
	USER_EMPTY_EXCEPTION(20211004,"您输入的账号不存在，请重新输入"),
	PASSWORD_ERROR_EXCEPTION(20211005,"您输入的密码有误，请重新输入"),
	ACCOUNT_INVALID_EXCEPTION(20211006,"账号已注销"),
	ACCOUNT_NO_PERMISSIONS_EXCEPTION(20211007,"账号无权限"),

	/**
	 * 根据员工id集合[%s]查询员工，全部不存在
	 */
	SAAS_OPERA_MANAGER_20212001(20211008, "根据员工id集合[%s]查询员工，全部不存在"),
	/**
	 * 离职员工无编辑业务
	 */
	SAAS_OPERA_MANAGER_20212002(20211009, "离职员工无编辑业务"),
	/**
	 * 入职时间不可大于今天
	 */
	SAAS_OPERA_MANAGER_20212003(20211010, "入职时间不可大于今天"),
	/**
	 * 该身份证号码[%s]对应员工已在入职员工中，请勿重复增加
	 */
	SAAS_OPERA_MANAGER_20212004(20211011, "该身份证号码[%s]对应员工已在入职员工中，请勿重复增加"),
	/**
	 * 该手机号码[%s]已经存在已存在，请重新输入
	 */
	SAAS_OPERA_MANAGER_20212005(20211012, "该手机号码[%s]已经存在，请重新输入"),
	/**
	 * 离职员工在入职，请走离职后在入职流程
	 */
	SAAS_OPERA_MANAGER_20212006(20211013, "错误的保存类型[%s]，离职员工在入职，保存类型应为[%s]"),
	/**
	 * 开通SaaS平台权限，请指定授予角色
	 */
	SAAS_OPERA_MANAGER_20212007(20211014, "开通SaaS平台权限，请指定授予角色"),
	/**
	 * 开通SaaS平台权限，指定授予角色不存在。ids=[%s]
	 */
	SAAS_OPERA_MANAGER_20212008(20211015, "开通SaaS平台权限，指定授予角色不存在。ids=[%s]"),
	/**
	 * 开通SaaS平台权限失败，失败角色id=[%s]
	 */
	SAAS_OPERA_MANAGER_20212009(20211016, "开通SaaS平台权限失败，失败角色id=[%s]"),
	/**
	 * 部门不存在或部门已禁用，部门id=[%s]
	 */
	SAAS_OPERA_MANAGER_20212010(20211017, "部门不存在或部门已禁用，部门id=[%s]"),
	/**
	 * 岗位不存在或岗位已禁用，岗位id=[%s]
	 */
	SAAS_OPERA_MANAGER_20212011(20211018, "岗位不存在或岗位已禁用，岗位id=[%s]"),

	/**
	 * 离职员工不支持授权操作
	 */
	SAAS_OPERA_MANAGER_20212012(20211019, "离职员工不支持授权操作，员工id=[%s]，员工手机号[%s]"),
	/**
	 * 已离职员工，不能再次离职
	 */
	SAAS_OPERA_MANAGER_20212013(20211020, "已离职员工，不能再次离职，员工id=[%s]，员工手机号[%s]"),
	/**
	 * 更新员工权限信息失败，员工id=[%s]
	 */
	SAAS_OPERA_MANAGER_20212014(20211021, "更新员工权限信息失败，员工id=[%s]"),
	/**
	 * 文件不存在
	 */
	SAAS_OPERA_MANAGER_20212015(20211022, "文件不存在!"),
	/**
	 * 上传OSS失败
	 */
	OSS_UPLOD_ERROR(20211023, "上传OSS失败！"),

	/**
	 * saveType=[%s],保存类型不正确!
	 */
	SAAS_OPERA_MANAGER_20212017(20211024, "saveType=[%s]，保存类型不正确!"),
	/**
	 * 离职员工中存在相同的身份证号码，不能重复新增。离职员工再次入职，参数缺少离职员工id
	 */
	SAAS_OPERA_MANAGER_20212018(20211025, "离职员工中存在相同的身份证号码[%s]，不能重复新增。离职员工再次入职，参数缺少离职员工id"),
	/**
	 * 岗位[%s]不属于部门[%s]
	 */
	SAAS_OPERA_MANAGER_20212019(20211026, "岗位[%s]不属于部门[%s]"),
	/**
	 * 请输入正确格式的身份证号
	 */
	SAAS_OPERA_MANAGER_20212020(20211027, "请输入正确格式的身份证号码"),
	SAAS_OPERA_MANAGER_20212021(20211028, "该身份证号已存在，不可重复创建"),
	/************************* 角色 *********************************/
	MENU_ID_ERROR(20102001,"权限菜单id非法"),
	HOME_PAGE_ID_ERROR(20102002,"工作台模块权限id非法"),
	ROLE_ID_ERROR(20102003,"角色id非法"),
	ROLE_APPROVAL_RETURN_SUCCESS(0,"审批退回执行成功!"),
	ADMINISTRATORS_NOT_SUPPORTED(20102004,"超级管理员不支持的操作"),
	ROLE_USER_NOT_SUPPORTED_LOGOUT(20102005,"该角色下存在在职状态的员工，不支持注销"),
	USER_CENTER_SERVICE_ERROR(20102006,"用户服务异常请稍后再试"),
	COMPANY_NOT_SUPPORT_REPEAT_INIT(20102007,"企业信息不支持重复初始化"),
	LOGOUT_ROLE_NOT_SUPPORT_EDIT(20102009,"该角色已注销，不支持编辑"),
	COMPANY_ID_ERROR(20102010,"企业id非法"),
	ROLE_NAME_EXISTS(20102011,"该角色名称已存在，请重新输入"),
	ROLE_DATE_COMPARETO(20102012,"查询的开始日期不能大于结束日期"),


	/****************** 业务异常错误码 START***************/
	SELECT_IS_EMPTY(30211001, "请选择需要操作的记录"),

	SERVICE_NAME_NOT_EMPTY(30211002,"该服务名称已经存在"),

	STATUS_ERROR(30211003,"状态错误"),

	DEPARTMENT_DISABLE_ERROR(30211004,"该部门及下级部门中存在启用中的岗位，不支持禁用！"),

	POSITION_NOTFOUND(30211005,"岗位不存在"),

	POSITION_DISABLE_ERROR(30211006,"该岗位及下级岗位中存在在职状态的员工，不支持禁用！"),

	PARENT_POSITION_NOTFOUND(30211007,"上级岗位不存在"),

	PARENT_POSITION_STATUS_ERROR(30211008,"上级岗位状态错误，未启用"),

	DEPARTMENT_NAME_ALREADY_EXIST(30211009,"部门名称已存在"),

	DEPARTMENTID_IS_EMPTY(30211010,"部门ID不能为空"),

	POSITION_NAME_EXISTS(30211011,"已存在该名称的岗位"),

	POSITION_CODE_EXISTS(30211012,"岗位编码已存在"),

	DEPARTMENT_PARENT_NOT_EXIST(30211013,"上级部门不存在"),

	DEPARTMENT_PARENT_NOT_IS_SELF(30211014,"上级部门不能为该部门本身"),

	/** 参数部门ID不能为空 */
	DEPT_ID_IS_NULL(30211015, "参数部门ID不能为空"),
	/** 参数岗位ID不能为空 */
	POSITION_ID_IS_NULL(30211016, "参数岗位ID不能为空"),

	PARENT_DEPARTMENT_STATUS_ERROR(30211017,"所属部门状态错误，未启用"),

	PARENT_POSITION_ISSELF(30211018,"上级岗位不能为本岗位"),

	DEPARTMENT_NOTFOUND(30211019,"部门不存在"),

	DEPARTMENT_CODE_ALREADY_EXIST(30211020,"部门编码已存在"),

	POSITION_NOTEDIT_DISABLED(30211021,"岗位已禁用，不能编辑"),

	PARENT_POSITION_ISEMPTY(30211022,"上级岗位不能为空"),

	POSITIONID_IS_EMPTY(30211023,"岗位ID不能为空"),

	DEPARTMENT_CANNOT_EDIT(30211024,"岗位不支持变更所属部门"),

	PARENT_POSITION_ISCHILD(30211025,"上级岗位不能是该岗位的下级岗位"),

	PARENT_DEPARTMENT_ISCHILD(30211026,"上级部门不能是该部门的下级部门"),

	ENABLE_PARENT_DEPT(30211027,"请先启用上级部门"),

	ENABLE_PARENT_POSITION(30211028,"请先启用上级岗位"),

    MSG_NOTFOUND(30211029,"留言不存在"),

    MSG_STATUS_ERROR(30211030,"状态错误，该留言已被处理"),

	SERVICE_REPEATED_OPENING_EXCEPTION(30211031, "企业已开通该服务");

	private int code;
	private String msg;

	CodeEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}

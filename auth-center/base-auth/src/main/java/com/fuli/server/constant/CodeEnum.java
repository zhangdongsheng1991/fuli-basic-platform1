package com.fuli.server.constant;

/**
 * 响应枚举
 *
 * @Author: XYJ
 * @CreateDate: 2019/7/5
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
     *
     *   20     salary-server
     *
     * 	 编码格式： 个数 8位
     * 	 第1位：  错误码级别
     * 	 第2-3位：区分业务线， 01-数据交换线，02-渠道业务线，03-薪资发放
     * 	 第4-5位：区分服务， 如上
     *	 第6-8位：序号--从001开始
     *
     *	 错误码级别
     *	 1：代表系统错误，如数据库异常、服务调用异常
     *	 2：代表参数错误，如手机号错误，身份证格式错误
     *	 3：代表业务异常，比如余额不足，库存不足等
     *	 4：代表权限异常，如没有调用权限等
     */

    /***************** 系统错误 ******************************/

    SUCCESS(0, "操作成功"),
    ERROR(10210000, "操作失败"),

    /**
     * @author WFZ  HttpRequestMethodNotSupportedException
     */
    HTTP_REQUEST_METHOD_EXCEPTION(10210001, "请求方法错误"),

    /**
     * @author WFZ  MissingServletRequestParameterException
     */
    REQUEST_PARAMETER_EXCEPTION(10204002, "缺少请求参数错误"),

    /**
     * @author WFZ  SQLSyntaxErrorException  SQL异常
     */
    SQL_SYNTAX_EXCEPTION(10204003, "SQL异常"),

    /**
     * @author WFZ  全局EXCEPTION
     */
    GLOBAL_EXCEPTION(10204004, "系统忙，请稍后再试或联系管理员"),
    REQUEST_GET_SUPPORTED(10210005, "请求不支持GET，请使用POST"),
    REQUEST_POST_SUPPORTED(10210006, "请求不支持POST，请使用GET"),



    PARAM_ERROR(20210000, "请求参数错误"),
    TYPE_CONVERT_ERROR(20210001, "参数类型转换错误"),
    EXCEPTION(20210002, "接口请求异常"),
    /**
     * 参数输入不合法！
     */
    SYSTEM_SERVER_20204001(20204003, "参数输入不合法"),

    /**
     * @author WFZ
     */
    MAX_ADD_QUICK_MODULE_ERROR(30204001, "工作台最多支持添加11个功能，如需继续添加，请先移出不常用的功能"),
    /**
     * @author WFZ
     */
    MINIMUN_ONLY_ONE_ERROR(30204002, "请至少保留一个快捷功能"),



   ;


    private int code;
    private String msg;

    CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}

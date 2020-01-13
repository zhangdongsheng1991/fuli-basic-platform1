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
    ERROR(10214000, "操作失败"),

    /**
     * @author WFZ  HttpRequestMethodNotSupportedException
     */
    HTTP_REQUEST_METHOD_EXCEPTION(10214001, "请求方法错误"),

    /**
     * @author WFZ  MissingServletRequestParameterException
     */
    REQUEST_PARAMETER_EXCEPTION(10214002, "缺少请求参数错误"),

    /**
     * @author WFZ  SQLSyntaxErrorException  SQL异常
     */
    SQL_SYNTAX_EXCEPTION(10214003, "SQL异常"),

    /**
     * @author WFZ  全局EXCEPTION
     */
    GLOBAL_EXCEPTION(10214004, "系统忙，请稍后再试或联系管理员"),

    /** 内部异常 */
    ERROR_CODE_500(10214011, "内部异常"),

    PARAM_ERROR(20214000, "请求参数错误"),
    TYPE_CONVERT_ERROR(20214001, "参数类型转换错误"),
    SYSTEM_SERVER_20204001(20214003, "参数输入不合法"),

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

package com.fuli.server.exception;

import lombok.Getter;

/**
 * @Author create by XYJ
 * @Date 2019/5/13 10:27
 **/
@Getter
public enum SystemErrorType implements ErrorType {
    /**
     * 	 WFZ -- 请各位尽量按规格编码
     * 	 编码 -- 具体服务
     * 	 10    auth-server
     * 	 11    saas-system
     * 	 12    api-gateway
     *
     *   编码格式： 个数 8位
     * 	 第1位：  错误码级别
     * 	 第2-3位：区分业务线， 01-数据交换先，02-渠道业务线
     * 	 第4-5位：区分服务， 如上
     *	 第6-8位：序号--从001开始
     *
     *	 错误码级别
     *	 1：代表系统错误，如数据库异常、服务调用异常
     *	 2：代表参数错误，如手机号错误，身份证格式错误
     *	 3：代表业务异常，比如余额不足，库存不足等
     *	 4：代表权限异常，如没有调用权限等
     */

    /** 系统处理成功 */
    SYS_PROCESS_SUCCESS(0, "系统处理成功"),
    SYSTEM_ERROR(10212000, "系统繁忙，请联系管理员"),
    SYSTEM_BUSY(10212001, "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE(10212002, "服务未找到"),
    GATEWAY_ERROR(10212003, "网关异常"),
    GATEWAY_CONNECT_TIME_OUT(10212004, "网关超时"),
    ILLEGAL_TOKEN(10212005, "非法令牌"),

    /**
     * @Author WFZ 无效token
     */
    INVALID_TOKEN_ERROR(1011,"无效token"),

    ARGUMENT_NOT_VALID(20212001, "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT(20212002, "上传文件大小超过限制");

    /**
     * 错误类型码
     */
    private int code;
    /**
     * 错误类型描述信息
     */
    private String msg;

    SystemErrorType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

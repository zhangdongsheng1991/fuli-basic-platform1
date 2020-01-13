package com.fuli.task.server.exception;


import com.fuli.task.server.constant.CodeEnum;

/**
 * 自定义业务异常
 *
 * @Author: XYJ
 * @CreateDate: 2019/7/5
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    protected String code;

    private static final long serialVersionUID = 3160241586346324994L;

    public BusinessException() {
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
        this.code = code;
    }


    public BusinessException(CodeEnum codeEnum, Object... args) {
        super(String.format(codeEnum.getMsg(), args));
        this.code = String.valueOf(codeEnum.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = String.valueOf(code);
    }
}

package com.fuli.task.server.exception;

/**
 * @Author create by XYJ
 * @Date 2019/10/11 12:52
 **/
public class OpenException extends RuntimeException {

    private static final long serialVersionUID = 3655050728585279326L;

    private int code = ErrorCode.ERROR.getCode();

    public OpenException() {

    }

    public OpenException(String msg) {
        super(msg);
    }

    public OpenException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public OpenException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}

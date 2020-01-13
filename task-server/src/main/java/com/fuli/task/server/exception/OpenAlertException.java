package com.fuli.task.server.exception;


/**
 * 提示消息异常
 * @Author create by XYJ
 * @Date 2019/10/11 12:52
 **/
public class OpenAlertException extends OpenException {
    private static final long serialVersionUID = 4908906410210213271L;

    public OpenAlertException() {
    }

    public OpenAlertException(String msg) {
        super(msg);
    }

    public OpenAlertException(int code, String msg) {
        super(code, msg);
    }

    public OpenAlertException(int code, String msg, Throwable cause) {
        super(code, msg, cause);
    }
}

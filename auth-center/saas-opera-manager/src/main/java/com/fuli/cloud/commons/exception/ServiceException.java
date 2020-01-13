package com.fuli.cloud.commons.exception;

import com.fuli.cloud.commons.CodeEnum;

/**
 * 必须继承RuntimeException，这样事务才能在抛出这个异常时发生事务回滚
 * @author yhm
 * @date 2019/05/28
 */
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;

    public ServiceException(int code, String msg) {
//        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public ServiceException(CodeEnum e) {
        super(e.getMsg());
        this.code = e.getCode();
        this.msg=e.getMsg();
    }

    public ServiceException(CodeEnum e, Object... args) {
        super(String.format(e.getMsg(), args));
        this.code = e.getCode();
        this.msg = String.format(e.getMsg(), args);
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
    
}

package com.fuli.task.server.exception;
/**
 * @Author create by XYJ
 * @Date 2019/5/13 10:27
 **/
public interface ErrorType {
    /**
     * 返回code
     *
     * @return
     */
    int getCode();

    /**
     * 返回mesg
     *
     * @return
     */
    String getMsg();
}

package com.fuli.cloud.commons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**
 * @Description: 统一响应对象
 * @Author: FZ
 * @CreateDate: 2019/4/15 18:54
 * @Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -993309571681498675L;

    @ApiModelProperty(name = "data",value = "响应数据")
    private T data;

    @ApiModelProperty(name = "code",value = "响应编码")
    private int code;

    @ApiModelProperty(name = "msg",value = "响应消息")
    private String msg;

    private static final Result<?> ok = new Result<>(CodeEnum.SUCCESS);
    private static final Result<?> fail = new Result<>(CodeEnum.ERROR);

    @JsonIgnore
    public boolean checkIs200() {
        return code == CodeEnum.SUCCESS.getCode();
    }

    /**
     * 操作成功，返回默认响应 code 0
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> succeed() {
        return (Result<T>) ok;
    }

    /**
     * 操作失败，返回默认响应 code -1
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> failed() {
        return (Result<T>) fail;
    }


    public Result(CodeEnum codeEnum) {
        this.data = null;
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }


    public static <T> Result<T> succeedMsg(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return succeedWith(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(T data) {
        return succeedWith(data, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg());
    }

    public static <T> Result<T> succeed(String msg) {
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> Result<T> succeed(Integer code, String msg) {
        return new Result<T>(null, code, msg);
    }

    public static <T> Result<T> succeedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    public static <T> Result<T> failed(String msg) {
        return failedWith(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(T data, String msg) {
        return failedWith(data, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> Result<T> failed(int code, String msg) {
        return failedWith(null, code, msg);
    }

    /**
     * 操作失败，传入CodeEnum
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> failed(CodeEnum codeEnum) {
        return new Result<T>(null, codeEnum.getCode(), codeEnum.getMsg());
    }

    public static <T> Result<T> failedWith(T data, Integer code, String msg) {
        return new Result<T>(data, code, msg);
    }

    public static <T> Result<T> failedWithArgs(CodeEnum codeEnum, Object... args) {
        return new Result<T>(null, codeEnum.getCode(), String.format(codeEnum.getMsg(), args));
    }


    /**
     * 根据状态返回成功失败
     * @author      WFZ
     * @param 	    flag
     * @return      Result
     * @date        2019/7/29 11:28
     */
    public static <T> Result<T> status(boolean flag) {
        return flag ? succeed("操作成功") : failed(CodeEnum.ILLEGAL_DATA_ERROR);
    }

    /**
     *  id 非空校验
     * @param id
     * @param <T>
     * @return
     */
    public static <T> Result<T> verifyId(Integer id) {
        if(null == id || id < 1){
            return Result.failed(CodeEnum.SELECT_IS_EMPTY);
        }
        return succeedWith(null, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg());
    }

}

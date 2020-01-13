package com.fuli.server.exception;

import com.fuli.server.model.Result;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * @Description:    统一异常处理
 * @Author:         XYJ
 * @CreateDate:     2019/11/23 16:20
 * @Version:        1.0
*/
@Slf4j
@RestControllerAdvice
public class GateWayExceptionHandlerAdvice {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public Result handle(ResponseStatusException ex) {
        log.error("响应状态异常:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_ERROR);
    }

    @ExceptionHandler(value = {BaseException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(BaseException ex) {
        log.error("自定义异常:{}",ex.getErrorType().getCode()+ "============" + ex.getErrorType().getMsg());
        return Result.fail(ex.getErrorType());
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public Result handle(ConnectTimeoutException ex) {
        log.error("连接超时异常:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_CONNECT_TIME_OUT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(NotFoundException ex) {
        log.error("没有找到异常:{}", ex.getMessage());
        return Result.fail(SystemErrorType.GATEWAY_NOT_FOUND_SERVICE);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(RuntimeException ex) {
        ex.printStackTrace();
        log.error("运行时异常:{}", ex);
        return Result.fail();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(Exception ex) {
        log.error("全局异常:{}", ex);
        return Result.fail();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.OK)
    public Result handle(Throwable throwable) {
        Result result = Result.fail();
        if (throwable instanceof ResponseStatusException) {
            result = handle((ResponseStatusException) throwable);
        }else if (throwable instanceof BaseException) {
            result = handle((BaseException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            result = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            result = handle((NotFoundException) throwable);
        } else if (throwable instanceof RuntimeException) {
            result = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            result = handle((Exception) throwable);
        }
        log.error("Throwable异常:{}", result.toString());
        return result;
    }
}

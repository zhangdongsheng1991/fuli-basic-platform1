package com.fuli.server.exception;

import com.fuli.server.base.Result;
import com.fuli.server.base.util.PublicUtil;
import com.fuli.server.constant.CodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.UnexpectedTypeException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;

/**
 * 全局异常处理
 *
 * @Author: XYJ
 * @CreateDate: 2019/7/5
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result illegalArgumentException(IllegalArgumentException e) {
        log.error("参数非法异常={}", e.getMessage(), e);
        return Result.failed(CodeEnum.SYSTEM_SERVER_20204001);
    }

    /**
     * 添加全局异常处理流程，根据需要设置需要处理的异常，本方法以MethodArgumentNotValidException为例
     * <p>
     * 参数校验异常
     *
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result methodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException exception) {
        //按需重新封装需要返回的错误信息
        List<FieldError> errorList = exception.getBindingResult().getFieldErrors();
        if (PublicUtil.isNotEmpty(errorList)) {
            return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(), errorList.get(0).getDefaultMessage());
        }

        return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(), "必填参数校验异常");
    }

    /**
     * @param exception
     * @return Result
     * @NotBlack 錯誤
     * @author WFZ
     * @date 2019/5/8 19:47
     */
    @ExceptionHandler(value = UnexpectedTypeException.class)
    @ResponseBody
    public Result methodArgumentNotValidHandler(UnexpectedTypeException exception) {
        return Result.failedWith(null, 500, exception.getMessage());
    }

    /**
     * 业务异常.
     *
     * @param e the e
     * @return the Response
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result businessException(BusinessException e) {
        log.error("业务异常={}", e.getMessage());
        return Result.failedWith(null, -1, e.getMessage());
    }
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result baseException(BaseException e) {
        log.error("业务异常={}", e.getErrorType().getMsg());
        return Result.failedWith(null, e.getErrorType().getCode(), e.getErrorType().getMsg());
    }


    /**
     * 参数类型转换错误 （使用post请求）
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler({HttpMessageConversionException.class})
    public Result parameterTypeException(HttpMessageConversionException exception) {
        log.error("post类型转换异常={}" + exception.getMessage());
        return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);

    }


    /**
     * 参数类型转换错误（使用对象接收的get请求）
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(BindException.class)
    public Result parameterTypeException(BindException exception) {
        log.error("get类型转换异常={}" + exception.getMessage());
        return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);

    }


    /**
     * 参数类型转换错误 （使用单个字段接收的get请求）
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result parameterTypeException(MethodArgumentTypeMismatchException exception) {
        log.error("单个字段接收的get请求类型转换异常={}" + exception.getMessage());
        return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);

    }

    /**
     * 参数类型转换错误 （使用单个字段接收的get请求）
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(NumberFormatException.class)
    public Result parameterTypeException(NumberFormatException exception) {
        log.error("类型转换异常={}" + exception.getMessage());
        return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);
    }

    /**
     * 缺少参数异常
     *
     * @author WFZ
     * @date 2019/5/17 10:54
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result parameterTypeException(MissingServletRequestParameterException exception) {
        log.error("缺少参数异常={}" + exception.getMessage());
        return Result.failedWith(null, CodeEnum.REQUEST_PARAMETER_EXCEPTION.getCode(), CodeEnum.REQUEST_PARAMETER_EXCEPTION.getMsg() + "==>" + exception.getMessage());
    }

    /**
     * 请求方法异常
     *
     * @author WFZ
     * @date 2019/5/17 10:54
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result parameterTypeException(HttpRequestMethodNotSupportedException exception) {
        log.error("请求方法异常={}" + exception.getMessage());
        return Result.failedWith(null, CodeEnum.HTTP_REQUEST_METHOD_EXCEPTION.getCode(), CodeEnum.HTTP_REQUEST_METHOD_EXCEPTION.getMsg() + "==>" + exception.getMessage());
    }

    /**
     * SQL异常
     *
     * @author WFZ
     * @date 2019/5/17 10:54
     */
    @ExceptionHandler(SQLSyntaxErrorException.class)
    public Result parameterTypeException(SQLSyntaxErrorException e) {
        log.error("SQL异常={}" + e.getMessage());
        return Result.failedWith(null, CodeEnum.SQL_SYNTAX_EXCEPTION.getCode(), CodeEnum.SQL_SYNTAX_EXCEPTION.getMsg() + "==>" + e.getMessage());
    }

    /**
     * 全局异常
     *
     * @param e the e
     * @return the Response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result exception(Exception e) {
        log.info("保存全局异常信息 ex={}", e.getMessage(), e);
        return Result.failedWith(null, CodeEnum.GLOBAL_EXCEPTION.getCode(), CodeEnum.GLOBAL_EXCEPTION.getMsg());
    }
}

package com.fuli.server.exception;

import com.fuli.server.base.Result;
import com.fuli.server.base.util.PublicUtil;
import com.fuli.server.constant.CodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
import java.util.Optional;

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
        return Result.failedWith(null, 10214011, exception.getMessage());
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
    @ExceptionHandler(BaseException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result exception(BaseException e) {
        log.info("保存全局异常信息 ex={}", e.getMessage(), e);
        return Result.failedWith(null, e.getErrorType().getCode(),e.getErrorType().getMsg());
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
        return Result.failedWith(null, CodeEnum.GLOBAL_EXCEPTION.getCode(),  e.getMessage());
    }

    /**
     * 静态解析认证异常
     *
     * @param ex
     * @return
     */
    public static Result resolveOauthException(Exception ex, String path) {
        ErrorCode code = ErrorCode.BAD_CREDENTIALS;
        int httpStatus = HttpStatus.OK.value();
        String error = Optional.ofNullable(ex.getMessage()).orElse("");
        if (error.contains("User is disabled")) {
            code = ErrorCode.ACCOUNT_DISABLED;
        }
        return buildBody(ex, code);
    }

    /**
     * 静态解析异常。可以直接调用
     *
     * @param ex
     * @return
     */
    public static Result resolveException(Exception ex, String path) {
        ErrorCode code = ErrorCode.ERROR;
        int httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
        String message = ex.getMessage();
        String superClassName = ex.getClass().getSuperclass().getName();
        String className = ex.getClass().getName();
        if (className.contains("UsernameNotFoundException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.USERNAME_NOT_FOUND;
        } else if (className.contains("BadCredentialsException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.BAD_CREDENTIALS;
        } else if (className.contains("AccountExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_EXPIRED;
        } else if (className.contains("LockedException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_LOCKED;
        } else if (className.contains("DisabledException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.ACCOUNT_DISABLED;
        } else if (className.contains("CredentialsExpiredException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.CREDENTIALS_EXPIRED;
        } else if (className.contains("InvalidClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_CLIENT;
        } else if (className.contains("UnauthorizedClientException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.UNAUTHORIZED_CLIENT;
        }else if (className.contains("InsufficientAuthenticationException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.UNAUTHORIZED;
        } else if (className.contains("InvalidGrantException")) {
            code = ErrorCode.ALERT;
            if ("Bad credentials".contains(message)) {
                code = ErrorCode.BAD_CREDENTIALS;
            } else if ("User is disabled".contains(message)) {
                code = ErrorCode.ACCOUNT_DISABLED;
            } else if ("User account is locked".contains(message)) {
                code = ErrorCode.ACCOUNT_LOCKED;
            }
        } else if (className.contains("InvalidScopeException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_SCOPE;
        } else if (className.contains("InvalidTokenException")) {
            httpStatus = HttpStatus.UNAUTHORIZED.value();
            code = ErrorCode.INVALID_TOKEN;
        } else if (className.contains("InvalidRequestException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.INVALID_REQUEST;
        } else if (className.contains("RedirectMismatchException")) {
            code = ErrorCode.REDIRECT_URI_MISMATCH;
        } else if (className.contains("UnsupportedGrantTypeException")) {
            code = ErrorCode.UNSUPPORTED_GRANT_TYPE;
        } else if (className.contains("UnsupportedResponseTypeException")) {
            code = ErrorCode.UNSUPPORTED_RESPONSE_TYPE;
        } else if (className.contains("UserDeniedAuthorizationException")) {
            code = ErrorCode.ACCESS_DENIED;
        } else if (className.contains("AccessDeniedException")) {
            code = ErrorCode.ACCESS_DENIED;
            httpStatus = HttpStatus.FORBIDDEN.value();
            if (ErrorCode.ACCESS_DENIED_BLACK_LIMITED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_BLACK_LIMITED;
            } else if (ErrorCode.ACCESS_DENIED_WHITE_LIMITED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_WHITE_LIMITED;
            } else if (ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED;
            }else if (ErrorCode.ACCESS_DENIED_UPDATING.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_UPDATING;
            }else if (ErrorCode.ACCESS_DENIED_DISABLED.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_DISABLED;
            } else if (ErrorCode.ACCESS_DENIED_NOT_OPEN.getMessage().contains(message)) {
                code = ErrorCode.ACCESS_DENIED_NOT_OPEN;
            }
        } else if (className.contains("HttpMessageNotReadableException")
                || className.contains("TypeMismatchException")
                || className.contains("MissingServletRequestParameterException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.BAD_REQUEST;
        } else if (className.contains("NoHandlerFoundException")) {
            httpStatus = HttpStatus.NOT_FOUND.value();
            code = ErrorCode.NOT_FOUND;
        } else if (className.contains("HttpRequestMethodNotSupportedException")) {
            httpStatus = HttpStatus.METHOD_NOT_ALLOWED.value();
            code = ErrorCode.METHOD_NOT_ALLOWED;
        } else if (className.contains("HttpMediaTypeNotAcceptableException")) {
            httpStatus = HttpStatus.BAD_REQUEST.value();
            code = ErrorCode.MEDIA_TYPE_NOT_ACCEPTABLE;
        } else if (className.contains("MethodArgumentNotValidException")) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            code = ErrorCode.ALERT;
            return Result.failedWith(null,code.getCode(),bindingResult.getFieldError().getDefaultMessage());
        } else if (className.contains("IllegalArgumentException")) {
            //参数错误
            code = ErrorCode.ALERT;
            httpStatus = HttpStatus.BAD_REQUEST.value();
        } else if (className.contains("OpenAlertException")) {
            code = ErrorCode.ALERT;
        } else if (className.contains("OpenSignatureException")) {
            code = ErrorCode.SIGNATURE_DENIED;
        }else if(message.equalsIgnoreCase(ErrorCode.TOO_MANY_REQUESTS.name())){
            code = ErrorCode.TOO_MANY_REQUESTS;
        }
        return buildBody(ex, code);
    }

    /**
     * 构建返回结果对象
     *
     * @param exception
     * @return
     */
    private static Result buildBody(Exception exception, ErrorCode resultCode) {
        if (resultCode == null) {
            resultCode = ErrorCode.ERROR;
        }
        Result resultBody = Result.failedWith(null,resultCode.getCode(),exception.getMessage());
        log.error("==> error:{} exception: {}",resultBody, exception);
        return resultBody;
    }
}

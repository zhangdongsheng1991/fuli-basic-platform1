package com.fuli.cloud.handler;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
 * @Description:    异常拦截器
 * @Author:         WFZ
 * @CreateDate:     2019/5/8 19:51
 * @Version:        1.0
*/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result illegalArgumentException(IllegalArgumentException e) {
		log.error("参数非法异常={}", e.getMessage(), e);
		return Result.failed(CodeEnum.SYSTEM_SERVER_2020060001);
	}

	/**
	 * 添加全局异常处理流程，根据需要设置需要处理的异常，本方法以MethodArgumentNotValidException为例
	 *
	 * 参数校验异常
	 *
	 * @param request
	 * @param exception
	 * @return
	 * @throws Exception
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.OK)
	public Object MethodArgumentNotValidHandler(HttpServletRequest request, MethodArgumentNotValidException exception) throws Exception {
		//按需重新封装需要返回的错误信息
		StringBuilder sb = new StringBuilder();
		/**
		 * 解析原错误信息，封装后返回，此处返回非法的字段名称，原始值，错误信息
		 *         error.getDefaultMessage();
		 *         error.getField();
		 *         error.getRejectedValue();
		 */
		for (FieldError error : exception.getBindingResult().getFieldErrors()) {
			sb.append(error.getDefaultMessage());
			break;
		}

		return Result.failedWith(null, CodeEnum.PARAM_ERROR.getCode(),sb.toString());
	}

	/**
	 * @NotBlack 錯誤
	 * @author      WFZ
	 * @param 	    exception
	 * @return      Result
	 * @date        2019/5/8 19:47
	 */
	@ExceptionHandler(value = UnexpectedTypeException.class)
	public Object MethodArgumentNotValidHandler(UnexpectedTypeException exception) throws Exception {
		return Result.failedWith(null,500,exception.getMessage());
	}

	/**
	 * 自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = CustomException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result customException(CustomException e){
		log.error("raised exception : " + e);
		return new Result(null ,e.getCodeEnum().getCode(), e.getCodeEnum().getMsg());
	}

	/**
	 * 参数类型转换错误（使用对象接收的get请求）
	 *
	 * @param exception 错误
	 * @return 错误信息
	 */
	@ExceptionHandler(BindException.class)
	public Result parameterTypeException(BindException exception){
		log.error("类型转换异常={}"+exception.getMessage());
		return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);

	}


	/**
	 * 参数类型转换错误 （使用单个字段接收的get请求）
	 *
	 * @param exception 错误
	 * @return 错误信息
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Result parameterTypeException(MethodArgumentTypeMismatchException exception){
		log.error("类型转换异常={}"+exception.getMessage());
		return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);

	}

	/**
	 * 参数类型转换错误 （使用单个字段接收的get请求）
	 *
	 * @param exception 错误
	 * @return 错误信息
	 */
	@ExceptionHandler(NumberFormatException.class)
	public Result parameterTypeException(NumberFormatException exception){
		log.error("类型转换异常={}"+exception.getMessage());
		return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);
	}

	/**
	 * 缺少参数异常
	 * @author      WFZ
	 * @date        2019/5/17 10:54
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public Result parameterTypeException(MissingServletRequestParameterException exception){
		log.error("缺少参数异常={}"+exception.getMessage());
		return Result.failedWith(null,CodeEnum.REQUEST_PARAMETER_EXCEPTION.getCode(),CodeEnum.REQUEST_PARAMETER_EXCEPTION.getMsg() +"==>"+exception.getMessage());
	}

	/**
	 * 请求方法异常
	 * @author      WFZ
	 * @date        2019/5/17 10:54
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Result parameterTypeException(HttpRequestMethodNotSupportedException exception){
		log.error("请求方法异常={}"+exception.getMessage());
		return Result.failedWith(null,CodeEnum.HTTP_REQUEST_METHOD_EXCEPTION.getCode(),CodeEnum.HTTP_REQUEST_METHOD_EXCEPTION.getMsg() +"==>"+exception.getMessage());
	}

	/**
	 * SQL异常
	 * @author      WFZ
	 * @date        2019/5/17 10:54
	 */
	@ExceptionHandler(SQLSyntaxErrorException.class)
	public Result parameterTypeException(SQLSyntaxErrorException e){
		log.error("SQL异常={}"+e.getMessage());
		return Result.failedWith(null,CodeEnum.SQL_SYNTAX_EXCEPTION.getCode(),CodeEnum.SQL_SYNTAX_EXCEPTION.getMsg() +"==>"+e.getMessage());
	}

	/**
	 * 自定义异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler(ServiceException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result serviceException(ServiceException e) {
		log.error("自定义异常={}", e);
		return Result.failedWith(null,e.getCode(),e.getMsg());
	}

	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Result httpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.error("raised exception : " + ex);
		ex.printStackTrace();
		Throwable cause = ex.getCause();
		return handleMisMatchedInputException(cause);
	}

	@ExceptionHandler({HttpMessageConversionException.class})
	public Result<?> parameterTypeException(HttpMessageConversionException ex) {

		log.error("raised exception : " + ex);
		ex.printStackTrace();
		Throwable cause = ex.getCause();
		return handleMisMatchedInputException(cause);
	}

	private Result handleMisMatchedInputException(Throwable cause) {
		if (cause instanceof JsonMappingException) {
			JsonMappingException e = (JsonMappingException) cause;
			List<JsonMappingException.Reference> path = e.getPath();
			if (path == null || path.size() == 0) {
				return Result.failed(e.getMessage());
			}
			JsonMappingException.Reference reference = path.get(0);
			return Result.failed("参数名称[" + reference.getFieldName() + "]的 值类型错误！请严格按照文档定义参数类型入参！");
		}
		return Result.failed(CodeEnum.TYPE_CONVERT_ERROR);
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
		return Result.failed(CodeEnum.GLOBAL_EXCEPTION);
	}
}

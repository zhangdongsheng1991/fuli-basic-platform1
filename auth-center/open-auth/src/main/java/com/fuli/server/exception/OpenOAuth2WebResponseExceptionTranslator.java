package com.fuli.server.exception;

import com.fuli.server.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 自定义oauth2异常提示
 * @author liuyadu
 */
@Slf4j
public class OpenOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    @Override
    public ResponseEntity translate(Exception exception) throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Result resultBody = GlobalExceptionHandler.resolveException(exception,request.getRequestURI());
        return ResponseEntity.status(200).body(resultBody);
    }
}

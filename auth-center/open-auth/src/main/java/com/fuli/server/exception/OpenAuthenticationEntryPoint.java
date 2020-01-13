package com.fuli.server.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuli.server.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无效token异常
 *
 * @author create by xyj
 */
@Slf4j
public class OpenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final ObjectMapper objectMapper=new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {
        Result resultBody = GlobalExceptionHandler.resolveException(exception,request.getRequestURI());

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultBody));
        response.getWriter().flush();
        response.getWriter().close();
    }
}
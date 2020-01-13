package com.fuli.server.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fuli.server.base.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足时调用
 *
 * @Author create by XYJ
 * @Date 2019/9/5 19:11
 **/

public class OpenAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exception) throws IOException, ServletException {
        Result resultBody = GlobalExceptionHandler.resolveException(exception, request.getRequestURI());

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(resultBody));
        response.getWriter().flush();
        response.getWriter().close();
    }
}


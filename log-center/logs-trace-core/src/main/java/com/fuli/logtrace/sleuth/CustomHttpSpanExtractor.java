package com.fuli.logtrace.sleuth;

import brave.Span;
import brave.Tracer;
import com.fuli.logtrace.constant.TraceConstant;
import org.slf4j.MDC;
import org.springframework.cloud.sleuth.instrument.web.TraceWebServletAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 自定义TraceId
 *
 * @Author create by XYJ
 * @Date 2019/6/27 16:09
 **/
@Component
@Order(TraceWebServletAutoConfiguration.TRACING_FILTER_ORDER + 1)
public class CustomHttpSpanExtractor extends GenericFilterBean {

    private final Tracer tracer;
    CustomHttpSpanExtractor(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        Span currentSpan = this.tracer.currentSpan();
        if (currentSpan == null) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        //从请求头中获取自定义traceID
        String clientIp = httpRequest.getHeader(TraceConstant.TRACE_LOG_HEADER);
        //把自定义traceID设置到MDC中，用于在logback配置中获取
        MDC.put(TraceConstant.X_B3_TRACEId,clientIp);
        chain.doFilter(request, response);
        MDC.remove(TraceConstant.X_B3_TRACEId);

    }}

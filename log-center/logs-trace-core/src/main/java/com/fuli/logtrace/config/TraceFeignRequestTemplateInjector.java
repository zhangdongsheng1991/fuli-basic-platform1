package com.fuli.logtrace.config;

import com.fuli.logtrace.constant.TraceConstant;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

/**
 * feign调用，传递traceId
 * @Author create by XYJ
 * @Date 2019/6/27 11:39
 **/
public class TraceFeignRequestTemplateInjector {

    public static void inject(RequestTemplate carrier) {
        String clientIp = MDC.get(TraceConstant.X_B3_TRACEId);
        setHeader(carrier, TraceConstant.TRACE_LOG_HEADER, clientIp);
    }

    protected static void setHeader(RequestTemplate request, String name,String value) {
        if (StringUtils.hasText(value) && !request.headers().containsKey(name)) {
            request.header(name, value);
        }
    }
}

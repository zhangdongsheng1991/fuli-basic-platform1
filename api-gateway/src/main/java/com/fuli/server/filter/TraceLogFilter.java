package com.fuli.server.filter;

import brave.Tracer;
import com.alibaba.fastjson.JSON;
import com.fuli.logtrace.constant.TraceConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 日志追踪TraceId从网关向下传递
 *
 * @Author create by XYJ
 * @Date 2019/6/27 10:14
 **/
@Component
@Slf4j
public class TraceLogFilter implements GlobalFilter, Ordered {
    private final static String X_CLIENT_TOKEN_USER = "x-client-token-user";

    @Autowired
    Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String requestUser = request.getHeaders().getFirst(X_CLIENT_TOKEN_USER);
        log.debug("requestUser:{}", requestUser);
        if (StringUtils.isNotEmpty(requestUser)) {
            String user = "";
            try {
                user = URLDecoder.decode(requestUser, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("URLDecoder error:{}", e);
            }

            Map jwtClientMap = (Map) JSON.parse(user);
            String userId="nouser";
            if (jwtClientMap.get("id")!=null){
               userId = jwtClientMap.get("id").toString();
            }
            // traceId
            String traceId = tracer.currentSpan().context().traceIdString() + "|uid-" + userId;
            ServerHttpRequest.Builder builder = request.mutate();
            builder.header(TraceConstant.TRACE_LOG_HEADER, traceId);
            MDC.put(TraceConstant.X_B3_TRACEId, traceId);
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        MDC.put(TraceConstant.X_B3_TRACEId, tracer.currentSpan().context().traceIdString());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 20000;
    }
}

package com.fuli.logtrace.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * feign请求拦截器，向下传递TraceId
 *
 * @Author create by XYJ
 * @Date 2019/6/27 11:37
 **/
public class TraceFeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        TraceFeignRequestTemplateInjector.inject(template);
    }
}

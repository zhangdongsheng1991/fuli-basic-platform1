package com.fuli.logtrace.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign配置注册（全局）
 * @Author create by XYJ
 * @Date 2019/7/2 11:13
 **/
@Configuration
public class LogTraceFeignSupportConfig {

    /**
     * feign请求拦截器
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor(){
        return new TraceFeignRequestInterceptor();
    }

}

package com.fuli.server.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.TimeZone;

/**
 * 网关配置
 *
 * @Author create by XYJ
 * @Date 2019/8/19 14:57
 **/
@Slf4j
@Configuration
public class GatewayConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    /**
     * Jackson全局配置
     *
     * @param properties
     * @return
     */
    @Bean
    @Primary
    public JacksonProperties jacksonProperties(JacksonProperties properties) {
        properties.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        properties.getSerialization().put(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        properties.setDateFormat("yyyy-MM-dd HH:mm:ss");
        properties.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return properties;
    }



}

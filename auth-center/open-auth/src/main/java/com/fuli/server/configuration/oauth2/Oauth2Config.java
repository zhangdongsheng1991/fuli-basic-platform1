package com.fuli.server.configuration.oauth2;

import com.fuli.server.configuration.oauth2.token.OpenTokenEnhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * @Author create by XYJ
 * @Date 2019/8/13 15:42
 **/
@Configuration
public class Oauth2Config {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 使用redis存储令牌
     * @return
     */
    @Bean
    public TokenStore tokenStore(){
        return new RedisTokenStore(connectionFactory);
    }

    /**
     * 授权码
     *
     * @return
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * 返回自定义token信息
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new OpenTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称加密
        converter.setSigningKey("fuliabc");
        // 必须要否则用户信息为空
//        converter.setAccessTokenConverter(new CustomerAccessTokenConverter());
        return converter;
    }

}

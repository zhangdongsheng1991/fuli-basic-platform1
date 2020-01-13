package com.fuli.cloud.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * https://www.jianshu.com/p/1a0a5c92185e
 * 启动有加载顺序 ， 如果bean注入顺序错误会无效 -- 例：connectionFactory
 * 所以需注入bean 的单独放在此类
 *
 * @Description:    注入bean
 * @Author:         WFZ
 * @CreateDate:     2019/9/10 12:46
 * @Version:        1.0
*/
@Configuration
public class Oauth2Config {

    @Autowired
    private RedisConnectionFactory connectionFactory;


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


}

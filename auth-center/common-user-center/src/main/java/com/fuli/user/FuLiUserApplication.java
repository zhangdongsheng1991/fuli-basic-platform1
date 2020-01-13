package com.fuli.user;

import com.fuli.logtrace.annotation.EnableLogTraceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.TimeZone;

/**
 * @Description:    启动类
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 16:12
 * @Version:        1.0
*/
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableLogTraceConfig
@MapperScan("com.fuli.user.dao.*")
public class FuLiUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FuLiUserApplication.class, args);
    }

    /**解决时区问题，参考：https://www.jianshu.com/p/085eb3c3120e*/
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Shanghai"))
                        .simpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}

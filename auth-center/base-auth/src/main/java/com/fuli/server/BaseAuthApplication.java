package com.fuli.server;

import com.fuli.logtrace.annotation.EnableLogTraceConfig;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author create by XYJ
 * @Date 2019/7/5 10:31
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient //注册中心客户端
@EnableFeignClients //开启feign调用
@EnableLogTraceConfig //开启日志追踪
@MapperScan(basePackages = "com.fuli.server.mapper.*")
public class BaseAuthApplication {
    public static void main(String[] args){
        SpringApplication.run(BaseAuthApplication.class,args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
}

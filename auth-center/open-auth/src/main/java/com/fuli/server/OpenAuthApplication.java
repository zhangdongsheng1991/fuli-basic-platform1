package com.fuli.server;

import com.fuli.logtrace.annotation.EnableLogTraceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author create by XYJ
 * @Date 2019/7/5 10:31
 **/
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient //注册中心客户端
@EnableFeignClients //开启feign调用
@EnableLogTraceConfig //开启日志追踪
public class OpenAuthApplication {
    public static void main(String[] args){
        SpringApplication.run(OpenAuthApplication.class,args);
    }
}

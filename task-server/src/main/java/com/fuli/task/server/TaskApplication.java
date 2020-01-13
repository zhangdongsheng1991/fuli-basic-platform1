package com.fuli.task.server;

import com.fuli.logtrace.annotation.EnableLogTraceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;


/**
 * 任务调度服务
 * @Author create by XYJ
 * @Date 2019/10/11 12:52
 **/
@EnableLogTraceConfig //开启日志追踪
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class TaskApplication {

    /**
     * 开启 @LoadBalanced 与 Ribbon 的集成
     * @return
     */
//    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(TaskApplication.class, args);
    }
}

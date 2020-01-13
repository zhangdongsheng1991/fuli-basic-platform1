package com.fuli;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author create by XYJ
 * @Date 2019/8/26 16:37
 **/
@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class FuliAdminApplication {
    public static void main(String[] args){
        SpringApplication.run(FuliAdminApplication.class,args);
    }
}

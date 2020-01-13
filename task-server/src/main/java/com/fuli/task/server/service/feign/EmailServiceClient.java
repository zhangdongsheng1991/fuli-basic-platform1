package com.fuli.task.server.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @Author create by XYJ
 * @Date 2019/10/11 12:52
 **/
@Component
@FeignClient(value = "base-auth")
public interface EmailServiceClient {
}

package com.fuli.cloud.feign;

import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.cloud.commons.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description:    客户端
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 17:46
 * @Version:        1.0
*/
@FeignClient("base-auth")
public interface BaseAuthFeign {

    /**
     * 方法实现说明
     * @author      WFZ
     * @param 	    clientId : 客户端id
     * @return      Result
     * @date        2019/9/5 17:46
     */
    @GetMapping("/app/client/{clientId}/details")
    Result<CustomClientDetails> getAppClientInfo(@PathVariable("clientId") String clientId);
}

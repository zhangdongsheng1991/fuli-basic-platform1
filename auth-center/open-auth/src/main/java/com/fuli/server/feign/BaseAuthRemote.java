package com.fuli.server.feign;

import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.server.base.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("base-auth")
public interface BaseAuthRemote {

    @GetMapping("/app/client/{clientId}/details")
    Result<CustomClientDetails> getAppClientInfo(@PathVariable("clientId") String clientId);
}

package com.fuli.server.api.controller;

import com.fuli.auth.common.model.GatherApi;
import com.fuli.server.api.model.entity.BaseApiDO;
import com.fuli.server.api.service.GatherApiApiService;
import com.fuli.server.constants.RestTemplateConstant;
import com.fuli.server.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @Author create by XYJ
 * @Date 2019/9/2 15:34
 **/
@RestController
@RequestMapping("/gather")
public class GatherApiController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private GatherApiApiService gatherApiApiService;

    /**
     * 负载均衡
     */
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    /**
     * 采集服务api
     *
     * @param serviceId
     * @return
     */
    @GetMapping("/api")
    public Result gatherApi(@RequestParam("serviceId") String serviceId) throws Exception {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        if (serviceInstance == null ) {
            return Result.fail("无此服务:"+serviceId);
        }
        String url = String.format("%s%s", serviceInstance.getUri(), RestTemplateConstant.GATHER_API_STR);
        GatherApi[] apis = restTemplate.getForObject(url, GatherApi[].class);
        List<BaseApiDO> saved = gatherApiApiService.saveGatherApis(Arrays.asList(apis));
        return Result.success(saved);
    }

}

package com.fuli.user.constant;

import com.alibaba.fastjson.JSONObject;
import com.fuli.user.service.PlatformTokenService;
import com.fuli.user.vo.PlatformResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;

/**
* @Description: (设置消息头及请求第三方接口)
* @author fengjing
* @date 2019/4/23 15:22
* @version V1.0
*/
@Aspect
@Component
@Slf4j
public class TemplateRequest {

    @Autowired
    private PlatformTokenService platformTokenService;
    @Resource(name = "platformRestTemplate")
    private RestTemplate restTemplates;


    /**
     * @Description:(统一设置消息头及发送第三方请求)
     * @author      fengjing
     * No such property: code for class: Script1
     * @return
     * @date        2019/4/23 15:24
     * @param postParameters
     */
    public JSONObject unifiedTransmission(String trl, HashMap<String, Object> postParameters) {
        //设置消息头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type","application/json");
        headers.setBearerAuth(platformTokenService.getPlatformAccessToken());
        HttpEntity<HashMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        //发送请求第三方接口
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(trl, r, JSONObject.class);
    }

    /**
     * RequestBody 参数请求方式
     * @author      WFZ
     * @param 	    url
     * @param 	    postParameters
     * @return      Result
     * @date        2019/10/18 9:26
     */
    public PlatformResponse postForObject(String url, HashMap<String, Object> postParameters) {
        //设置消息头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.setBearerAuth(platformTokenService.getPlatformAccessToken());
        HttpEntity<HashMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        //发送请求第三方接口
        PlatformResponse response = restTemplates.postForObject(url, r, PlatformResponse.class);
        log.info("请求中台接口返回:{}",response);
        return response;
    }
    
    /**
     * 表单请求方式
     * @author      WFZ
     * @param       url
     * @param       params
     * @return      Result
     * @date        2019/10/18 9:27
     */
    public PlatformResponse postForObject(String url, MultiValueMap<String,String> params) {
        //设置消息头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(platformTokenService.getPlatformAccessToken());
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        PlatformResponse response = restTemplates.postForObject(url, requestEntity, PlatformResponse.class);
        log.info("请求中台接口返回:{}",response);
        return response;
    }
}

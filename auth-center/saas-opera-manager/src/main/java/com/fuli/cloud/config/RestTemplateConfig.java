package com.fuli.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @Description:    restTemplate工厂
 * @Author:         WFZ
 * @CreateDate:     2019/5/6 11:41
 * @Version:        1.0
*/
@Configuration
public class RestTemplateConfig {

    /**
     * // get 请求
     * public <T> T getForObject();
     * public <T> ResponseEntity<T> getForEntity();
     *
     * // head 请求
     * public HttpHeaders headForHeaders();
     *
     * // post 请求
     * public URI postForLocation();
     * public <T> T postForObject();
     * public <T> ResponseEntity<T> postForEntity();
     *
     * // put 请求
     * public void put();
     *
     * // pathch
     * public <T> T patchForObject
     *
     * // delete
     * public void delete()
     *
     * // options
     * public Set<HttpMethod> optionsForAllow
     *
     * // exchange
     * public <T> ResponseEntity<T> exchange()
     *
     *
     * @return restTemplate
     */
    @Bean("httpClient")
    public RestTemplate httpClientRestTemplate(){
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}

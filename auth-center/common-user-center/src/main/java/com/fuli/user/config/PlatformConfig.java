package com.fuli.user.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 集团中台用户配置
 *
 * @author pcg
 * @version 1.0
 * @date 2019-4-16 17:19:56
 */
@Configuration
@ConfigurationProperties(prefix = "platform")
@Data
public class PlatformConfig {

    /**
     * 中台服务地址
     */
    private String url;

    /**
     * 应用标识
     */
    private String clientId;

    /**H
     * 应用秘钥
     */
    private String secretKey;

    /**
     * 请求超时时间
     */
    @Value("${platform.http.requestTimeout}")
    private Integer httpRequestTimeout;

    /**
     * 连接超时时间
     */
    @Value("${platform.http.connectTimeout}")
    private Integer httpConnectTimeout;

    /**
     * 读取超时时间
     */
    @Value("${platform.http.readTimeout}")
    private Integer httpReadTimeout;

    /**
     * 中台rsa加密公钥
     */
    @Value("${platform.rsa.publicKey}")
    private String rsaPublicKey;

    /**
     * 中台接口返回正常错误码
     */
    private int responseOkCode = 10200;

    /**
     * 第三方登录的信息
     */
    private Map<String, Map<String, String>> third;

    @Bean(name = "platformRestTemplate")
    public RestTemplate platformRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        // 请求超时时间
        httpRequestFactory.setConnectionRequestTimeout(httpRequestTimeout);
        // 连接超时
        httpRequestFactory.setConnectTimeout(httpConnectTimeout);
        // 读取超时
        httpRequestFactory.setReadTimeout(httpReadTimeout);
        return new RestTemplate(httpRequestFactory);
    }
}

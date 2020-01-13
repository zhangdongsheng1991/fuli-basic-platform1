package com.fuli.user.service.impl;

import com.fuli.user.config.PlatformConfig;
import com.fuli.user.constant.PlatformApiUriConstant;
import com.fuli.user.dto.PlatformAccessTokenResponseDTO;
import com.fuli.user.service.PlatformTokenService;
import com.fuli.user.utils.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;


/**
 * 中台token服务实现
 * @author pcg
 * @date 2019-4-17 09:20:31
 * @version 1.0
 */
@Service
@Slf4j
public class PlatformTokenServiceImpl implements PlatformTokenService {


    @Resource(name = "platformRestTemplate")
    private RestTemplate restTemplate;
    @Autowired
    private PlatformConfig platformConfig;
    @Autowired
    private RedisService redisService;

    private final String PLATFORM_ACCESS_TOKEN = "platform_access_token";

    /**
     * 获取中台token  内存缓存AOP实现
     * @return
     */
    @Override
    public String getPlatformAccessToken() {
        String accessToken = "";
        if (redisService.exists(PLATFORM_ACCESS_TOKEN)){
            accessToken = redisService.get(PLATFORM_ACCESS_TOKEN) + "";
        }else {
            String url = platformConfig.getUrl() + PlatformApiUriConstant.GET_ACCESS_TOKEN;
            PlatformAccessTokenResponseDTO responseDto = null;
            try {
                responseDto = restTemplate.postForObject(url, new HttpEntity<>(getPlatformBasicAuthHeaders()), PlatformAccessTokenResponseDTO.class);
            }catch (HttpClientErrorException e){
                log.error("获取中台token异常,请求地址:{}",url);
            }
            // 检查返回的accessToken数据是否正常
            if (ObjectUtils.isEmpty(responseDto) || responseDto.getCode() != platformConfig.getResponseOkCode()){
                log.error("获取中台token返回结果错误,请求地址:{}",url);
            }
            PlatformAccessTokenResponseDTO.AccessTokenDto data = responseDto.getData();
            if (data != null){
                accessToken = data.getAccess_token();
                // token有效时间24H
                redisService.set(PLATFORM_ACCESS_TOKEN,accessToken,data.getExpires_in()-3);
            }
        }
        return accessToken;
    }


    /**
     * 设置中台api basicAuth请求头
     * @return 请求头数据
     */
    private HttpHeaders getPlatformBasicAuthHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(platformConfig.getClientId(), platformConfig.getSecretKey());
        return httpHeaders;
    }

}

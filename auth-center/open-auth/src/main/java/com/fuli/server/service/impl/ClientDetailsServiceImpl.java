package com.fuli.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.fuli.auth.common.constant.RedisKeyConstant;
import com.fuli.auth.common.model.CustomAuthority;
import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.server.feign.BaseAuthRemote;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Autowired
    private BaseAuthRemote baseAuthRemote;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        // 先从redis获取
        Object o = redisTemplate.boundHashOps(RedisKeyConstant.BASE_AUTH_CACHE_CLIENT_KEY).get(clientId);
        CustomClientDetails details = new CustomClientDetails();
        if (null != o){
            details = JSON.parseObject(JSON.toJSONString(o),CustomClientDetails.class);
            Map map = JSON.parseObject(JSON.toJSONString(o), Map.class);
            if (! map.isEmpty()){
                List<Map> o1 = (List<Map>)map.get("authorities");
                Collection<CustomAuthority> authorities = new ArrayList<>();
                if (o1 != null && o1.size()>0){
                    for (Map ma : o1){
                        CustomAuthority a = JSON.parseObject(JSON.toJSONString(ma), CustomAuthority.class);
                        ((ArrayList<CustomAuthority>) authorities).add(a);
                    }
                    details.setAuthorities(authorities);
                }
            }
        }
        if (null == details || StringUtils.isBlank(details.getClientId())){
            details =baseAuthRemote.getAppClientInfo(clientId).getData();
            redisTemplate.boundHashOps(RedisKeyConstant.BASE_AUTH_CACHE_CLIENT_KEY).put(clientId, details);
        }

        if (details != null && details.getAdditionalInformation() != null) {
            String status = details.getAdditionalInformation().getOrDefault("status", "0").toString();
            if(!"1".equals(status)){
                throw new ClientRegistrationException("客户端已被禁用");
            }
        }
        return details;
    }
}

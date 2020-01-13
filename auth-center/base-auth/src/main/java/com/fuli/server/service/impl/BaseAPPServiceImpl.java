package com.fuli.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.auth.common.constant.RedisKeyConstant;
import com.fuli.auth.common.model.CustomAuthority;
import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.auth.common.utils.DateUtil;
import com.fuli.server.mapper.AuthorityAppMapper;
import com.fuli.server.mapper.BaseAppMapper;
import com.fuli.server.model.dto.BaseAddAppDTO;
import com.fuli.server.model.entity.BaseAppDO;
import com.fuli.server.service.AuthorityApiService;
import com.fuli.server.service.BaseAPPService;
import com.fuli.server.utils.BeanConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Wrapper;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author create by XYJ
 * @Date 2019/8/7 11:40
 **/
@Service
@Slf4j
public class BaseAPPServiceImpl implements BaseAPPService {

    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;
    @Autowired
    private AuthorityAppMapper authorityAppMapper;
    @Autowired
    private BaseAppMapper baseAppMapper;
    @Autowired
    private AuthorityApiService authorityApiService;


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * token有效期，默认12小时
     */
    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
    /**
     * token有效期，默认7天
     */
    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;


    /**
     * 根据应用标识获取应用信息
     *
     * @param clientId 应用标识
     * @return
     */
    @Override
    public CustomClientDetails getDetailsByClientId(String clientId) {

        BaseClientDetails baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
        CustomClientDetails openClient = new CustomClientDetails();
        BeanUtils.copyProperties(baseClientDetails, openClient);
        List<CustomAuthority> authoritys = authorityAppMapper.getAuthorityByClientId(clientId);
        openClient.setAuthorities(authoritys);
        /**
         * 缓存权限分组API
         */
        authoritys.forEach((authority)->{
            authorityApiService.getApisByGroudId(authority.getAuthorityGroupId());
        });
        return openClient;
    }

    /**
     * 缓存client并返回client
     *
     * @param clientId
     * @return
     */
    private BaseClientDetails cacheAndGetClient(String clientId) {
        // 从数据库读取
        BaseClientDetails clientDetails = null;
        try {
            clientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
            if (clientDetails != null) {
                // 写入redis缓存
                redisTemplate.boundHashOps(RedisKeyConstant.BASE_AUTH_CACHE_CLIENT_KEY).put(clientId, JSONObject.toJSONString(clientDetails));
                log.info("缓存clientId:{},{}", clientId, clientDetails);
            }
        } catch (NoSuchClientException e) {
            log.info("clientId:{},{}", clientId, clientId);
        } catch (InvalidClientException e) {
            e.printStackTrace();
        }
        return clientDetails;
    }

    /**
     * 将oauth_client_details全表刷入redis
     */
    public void loadAllClientToCache() {
        if (redisTemplate.hasKey(RedisKeyConstant.BASE_AUTH_CACHE_CLIENT_KEY)) {
            return;
        }
        log.info("将oauth_client_details全表刷入redis");

        List<ClientDetails> list = jdbcClientDetailsService.listClientDetails();
        if (CollectionUtils.isEmpty(list)) {
            log.error("oauth_client_details表数据为空，请检查");
            return;
        }

        list.parallelStream().forEach(client -> {
            redisTemplate.boundHashOps(RedisKeyConstant.BASE_AUTH_CACHE_CLIENT_KEY).put(client.getClientId(), JSONObject.toJSONString(client));
        });
    }

    /**
     * 新增应用
     *
     * @param app 应用信息
     * @return
     */
    @Override
    @Transactional
    public BaseAppDO addApp(BaseAddAppDTO app) {

        String appId = String.valueOf(System.currentTimeMillis());
        String apiKey = RandomStringUtils.randomAlphanumeric(24);
        String secretKey = RandomStringUtils.randomAlphanumeric(32);
        app.setAppId(appId);
        app.setApiKey(apiKey);
        app.setSecretKey(secretKey);
        app.setCreateTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
        app.setUpdateTime(app.getCreateTime());
        if (app.getIsPersist() == null) {
            app.setIsPersist(0);
        }
        BaseAppDO appInfoDO = new BaseAppDO();
        BeanUtils.copyProperties(app, appInfoDO);
        baseAppMapper.insert(appInfoDO);
        Map info = BeanConvertUtils.objectToMap(app);
        // 功能授权
        BaseClientDetails client = new BaseClientDetails();
        client.setClientId(app.getApiKey());
        client.setClientSecret(app.getSecretKey());
        client.setAdditionalInformation(info);

        client.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "client_credentials", "implicit", "password"));
        client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
        client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
        jdbcClientDetailsService.addClientDetails(client);
        return appInfoDO;

    }

    @Override
    public BaseAppDO getInfoByClientId(String clientId) {
        QueryWrapper<BaseAppDO> wrapper=new QueryWrapper<>();
        wrapper.eq("api_key",clientId);
        BaseAppDO baseAppDO = baseAppMapper.selectOne(wrapper);
        return baseAppDO;
    }


}

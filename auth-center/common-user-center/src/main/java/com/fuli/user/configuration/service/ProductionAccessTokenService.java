package com.fuli.user.configuration.service;


import com.fuli.auth.common.model.BaseUser;
import com.fuli.auth.common.model.MyAuthenticationToken;
import com.fuli.user.commons.Result;
import com.fuli.user.constant.CommonConstant;
import com.fuli.user.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.HashSet;

/**
 * @Description:    生产Token
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 18:03
 * @Version:        1.0
*/
@Slf4j
@Service
public class ProductionAccessTokenService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private ClientDetailsService clientDetailsService;

    /**
     * 生产token
     * @author      WFZ
     * @param 	    clientId  : 客户端id
     * @param 	    clientSecret : 客户端密匙 --- 值为switchover 表示切换token，不校验客户端key
     * @param 	    grantType : 客户端模式
     * @param 	    baseUser : 封装token信息
     * @param 	    type : 区分来源，3-表示中台获取token 需要校验 clientSecret
     * @return      Result
     * @date        2019/5/30 21:44
     */
    public Result createAccessToken(@RequestParam("clientId") String clientId,
                                    @RequestParam("clientSecret") String clientSecret ,
                                    @RequestParam("grantType") String grantType ,
                                    @RequestBody BaseUser baseUser ,
                                    @RequestParam("type") int type) {
        log.info("生产token参数clientId:{} , grantType:{} , baseUser:{}",clientId, grantType, baseUser.toString());
        if (StringUtils.isBlank(clientId)) {
            return Result.failed(20210008, "客户端id不能为空");
        }
        /** 查询client*/
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if (null == clientDetails) {
            return Result.failed(10210001, "无效的客户端");
        }
        if (type == 3){
            if (! CommonConstant.CLIENT_SECRET_SWITCHOVER.equals(clientSecret) && !passwordEncoder.matches(clientSecret,clientDetails.getClientSecret())) {
                return Result.failed(10210001, "无效的客户端密匙");
            }
        }

        /** 生成token请求类 clientDetails.getScope()*/
        OAuth2Request oAuth2Request = null ;
        try{
            TokenRequest tokenRequest = new TokenRequest(new HashMap(1), clientId, new HashSet<>(), grantType );
            oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        }catch (Exception e){
            log.error("生成token请求类出错,",e.getMessage());
            return Result.failed(10210001, "无效的客户端模式");
        }
        /** 自定义token参数*/
        baseUser.setClientId(clientId);
        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(baseUser, baseUser.getAuthorities());
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authenticationToken);
        try {
            OAuth2AccessToken accessToken = tokenStore.getAccessToken(oAuth2Authentication);
            if (accessToken != null){
                if (accessToken.getRefreshToken() != null) {
                    this.tokenStore.removeRefreshToken(accessToken.getRefreshToken());
                }
                tokenStore.removeAccessToken(accessToken);
            }
        }catch (Exception e){
            log.error("清空原token出错",e);
        }
        AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil.getBean("defaultAuthorizationServerTokenServices", AuthorizationServerTokenServices.class);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        return Result.succeed(oAuth2AccessToken);
    }

}

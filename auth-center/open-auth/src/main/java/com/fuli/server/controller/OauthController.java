package com.fuli.server.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fuli.server.base.Result;
import com.fuli.server.exception.FuliAssert;
import com.fuli.server.util.SpringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * @Author create by XYJ
 * @Date 2019/8/13 14:48
 **/
@RestController
@RequestMapping("/client")
@Api(tags = "TOKEN管理")
@Slf4j
@Validated
public class OauthController {

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientDetailsService clientDetailsService;

    @ApiOperation(value = "客户端模式生成token")
    @RequestMapping(value = "/token")
    public OAuth2AccessToken createAccessToken(@NotEmpty(message = "参数appId不能为空") @RequestParam("appId") String appId,
                                               @NotEmpty(message = "参数secret不能为空") @RequestParam("secret") String secret,
                                               @NotEmpty(message = "参数grantType不能为空") @RequestParam("grantType") String grantType) {
        if (!"client_credentials".equals(grantType)) {
            FuliAssert.unauthorized(10210000, "不支持的授权模式");
        }

        /** 查询client*/
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(appId);

        if (null == clientDetails) {
            FuliAssert.unauthorized(10210001, "无效的客户端");
        }
        if (!passwordEncoder.matches(secret, clientDetails.getClientSecret())) {
            FuliAssert.unauthorized(10210001, "无效的客户端");
        }
        TokenRequest tokenRequest = new TokenRequest(new HashMap(1), appId, new HashSet<>(), grantType);
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        Collection<OAuth2AccessToken> oAuth2AccessTokens = tokenStore.findTokensByClientId(appId);
        if (oAuth2AccessTokens != null && oAuth2AccessTokens.size() > 0) {
            OAuth2AccessToken accessToken = oAuth2AccessTokens.iterator().next();
            tokenStore.removeAccessToken(accessToken);
        }
//        MyAuthenticationToken authenticationToken = new MyAuthenticationToken(null, clientDetails.getAuthorities());
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, null);
        AuthorizationServerTokenServices authorizationServerTokenServices = SpringUtil.getBean(DefaultTokenServices.class);
        OAuth2AccessToken oAuth2AccessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        oAuth2Authentication.setAuthenticated(true);
        return oAuth2AccessToken;
    }


    @ApiOperation(value = "获取access存储内容", httpMethod = "GET")
    @GetMapping("/deserialize/access")
    @SentinelResource(value = "sayHello")
    public Result deserializeAccessToken(
            @ApiParam("accessToken") @NotBlank(message = "accessToken不能为空")
            @RequestParam("accessToken") String accessToken) {
        log.info("++++++++++++++"+accessToken);
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        return Result.succeed(oAuth2Authentication);
    }

}

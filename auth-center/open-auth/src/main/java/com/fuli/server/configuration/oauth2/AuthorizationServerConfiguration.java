package com.fuli.server.configuration.oauth2;

import com.fuli.server.configuration.oauth2.token.OpenTokenEnhancer;
import com.fuli.server.exception.OpenOAuth2WebResponseExceptionTranslator;
import com.fuli.server.service.impl.BaseUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 平台认证服务器配置
 *
 * @author liuyadu
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager; //认证管理者

    @Autowired
    @Qualifier(value = "clientDetailsServiceImpl")
    private ClientDetailsService customClientDetailsService; //自定义获取客户端,为了支持自定义权限,

//    @Autowired
//    private BaseUserDetailService baseUserDetailService;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private TokenEnhancer tokenEnhancer;
    //jwt生成token
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    /**
     * 配置应用名称 应用id
     * 配置OAuth2的客户端相关信息
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(customClientDetailsService);
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore)
//                .userDetailsService(baseUserDetailService)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)
                //自定义异常
                .exceptionTranslator(new OpenOAuth2WebResponseExceptionTranslator());
        //自定义token生成方式
        endpoints.tokenServices(createDefaultTokenServices());


    }
    @Bean
    public DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
//        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(customClientDetailsService);
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        return tokenServices;
    }

    /**
     * 返回自定义token信息
     *
     * @return
     */
    @Bean
    public TokenEnhancer customerEnhancer() {
        return new OpenTokenEnhancer();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                // 开启表单认证
                .tokenKeyAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }


}

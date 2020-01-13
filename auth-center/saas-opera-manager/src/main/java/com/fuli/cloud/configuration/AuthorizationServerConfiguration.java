package com.fuli.cloud.configuration;

import com.fuli.cloud.configuration.service.BaseUserDetailServiceImpl;
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
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * 平台认证服务器配置
 *
 * @author liuyadu
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    /**
     * 认证管理者
     */
    @Autowired
    private AuthenticationManager authenticationManager;
    /**
     * 自定义获取客户端,为了支持自定义权限
     */
    @Autowired
    @Qualifier(value = "clientDetailsServiceImpl")
    private ClientDetailsService customClientDetailsService;
    @Autowired
    private BaseUserDetailServiceImpl baseUserDetailServiceImpl;
    @Autowired
    private TokenStore tokenStore;

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

    /**
     * 返回自定义token信息
     *
     * @return
     */
    @Bean
    public TokenEnhancer customerEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        //对称加密
        converter.setSigningKey("fuliabc");
        return converter;
    }


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints){
        endpoints
                .userDetailsService(baseUserDetailServiceImpl)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)
                //指定token存储位置
                .tokenStore(tokenStore);
        //自定义token生成方式
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(customerEnhancer(), jwtAccessTokenConverter()));
        endpoints.tokenEnhancer(tokenEnhancerChain);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()")
                // 开启表单认证
                .allowFormAuthenticationForClients();
    }

}

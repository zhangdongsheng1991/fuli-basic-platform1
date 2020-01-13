package com.fuli.auth.common.configuration;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;


public class OpenTokenEnhancer extends TokenEnhancerChain {

    /**
     * 生成token
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
//        final Map<String, Object> additionalInfo = new HashMap<>(8);
//        if (!authentication.isClientOnly()) {
//            if (authentication.getPrincipal() != null && authentication.getPrincipal() instanceof OpenUserDetails) {
//                // 设置额外用户信息
//                OpenUserDetails baseUser = ((OpenUserDetails) authentication.getPrincipal());
//                additionalInfo.put(SecurityConstants.OPEN_ID, baseUser.getUserId());
//                additionalInfo.put(SecurityConstants.DOMAIN, baseUser.getDomain());
//            }
//        }
//        defaultOAuth2AccessToken.setAdditionalInformation(additionalInfo);
        return super.enhance(accessToken, authentication);
    }
}

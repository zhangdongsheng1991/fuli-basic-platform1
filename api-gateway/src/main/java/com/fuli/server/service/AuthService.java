package com.fuli.server.service;

import org.springframework.security.jwt.Jwt;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public interface AuthService {
    /**
     * 是否是url白名单
     *
     * @param reqUrl 请求URL
     * @return
     */
    boolean isWhiteURL(String reqUrl);

    /**
     * 从认证信息中提取jwt token 对象
     *
     * @param authentication 认证信息  Authorization: bearer header.payload.signature
     * @return Jwt对象
     */
    Jwt getJwt(String authentication);

    /**
     * 鉴定url权限
     *
     * @param oAuth2Authentication
     * @param url
     * @param method
     * @return
     */
    boolean hasPermission(OAuth2Authentication oAuth2Authentication, String url, String method);
}

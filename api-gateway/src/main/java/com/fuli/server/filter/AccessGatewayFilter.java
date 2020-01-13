package com.fuli.server.filter;

import com.alibaba.fastjson.JSON;
import com.fuli.server.constants.UrlCommonConstant;
import com.fuli.server.exception.FuliAssert;
import com.fuli.server.exception.SystemErrorType;
import com.fuli.server.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 权限校验
 *
 * @Author create by XYJ
 * @Date 2019/5/6
 */
@Component
@Slf4j
public class AccessGatewayFilter implements GlobalFilter, Ordered {

    private final static String X_CLIENT_TOKEN_USER = "x-client-token-user";
    private final static String CLIENT_ID = "x-client-id";
    private final static String X_CLIENT_TOKEN = "x-client-token";
    /**
     * Authorization认证开头是"bearer "
     */
    private static final int BEARER_BEGIN_INDEX = 7;
    private final static String UTF8_STR = "UTF-8";

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private AuthService authService;

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
     * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> queryToken = request.getQueryParams().get("accessToken");
        String headToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        headToken=StringUtils.substring(headToken, BEARER_BEGIN_INDEX);
        String accessToken = queryToken == null ||  StringUtils.isEmpty(queryToken.get(0)) ? headToken : queryToken.get(0);

        String method = request.getMethodValue();
        String url = request.getPath().value();
        log.debug("url:{},method:{},headers:{}", url, method, request.getHeaders());
        ServerHttpRequest.Builder builder = request.mutate();
        // 中台不需要处理的URL  直接放行 /api-middle/api-platform/createTokenForPlatform
        if (pathMatcher.match(UrlCommonConstant.MID_WITH, url)) {
            return chain.filter(exchange);
        }
        OAuth2Authentication oAuth2Authentication = null;
        try{
            if (StringUtils.isNotBlank(accessToken)){
                oAuth2Authentication = tokenStore.readAuthentication(accessToken);
            }
        }catch (Exception e){
            log.error("网关获取OAuth2Authentication",e);
            FuliAssert.unauthorized(SystemErrorType.ILLEGAL_TOKEN.getCode(), "非法令牌");
        }
        //无需鉴权RUL，直接放行
        if (authService.isWhiteURL(url)) {
            //带上token时，用户信息向下传递
            if (StringUtils.isNotBlank(accessToken) && oAuth2Authentication != null) {
                String clientInfo = authService.getJwt(accessToken).getClaims();
                Map jwtClientMap = (Map) JSON.parse(clientInfo);
                if (jwtClientMap.get("id") != null) {
                    try {
                        builder.header(CLIENT_ID, URLEncoder.encode(jwtClientMap.get("client_id").toString(), UTF8_STR));
                    }catch (Exception ex){
                        log.error("x-client-token-user内容编码出错:{}", ex);
                    }

                    builder.header(X_CLIENT_TOKEN_USER, clientInfo);
                    return chain.filter(exchange.mutate().request(builder.build()).build());
                }
            }
            return chain.filter(exchange);
        }
        //请求token为空
        if (StringUtils.isBlank(accessToken)) {
            log.info("请求token为空无权限");
            FuliAssert.unauthorized(401, "非法请求，token不能为空");
        }

        if (oAuth2Authentication == null) {
            log.info("redis查无此token，token无效");
            FuliAssert.unauthorized(SystemErrorType.INVALID_TOKEN_ERROR.getCode(), SystemErrorType.INVALID_TOKEN_ERROR.getMsg());
        }

        //鉴定权限，若有权限进入下一个filter
        if (authService.hasPermission(oAuth2Authentication, url, method)) {
            try {
                String clientInfo = authService.getJwt(accessToken).getClaims();
                Map jwtClientMap = (Map) JSON.parse(clientInfo);
                builder.header(CLIENT_ID, URLEncoder.encode(jwtClientMap.get("client_id").toString(), UTF8_STR));
                /** 将jwt token中的用户信息传给服务 ， 转发的请求都加上服务间认证token  builder.header(X_CLIENT_TOKEN);*/
                builder.header(X_CLIENT_TOKEN_USER, URLEncoder.encode(authService.getJwt(accessToken).getClaims(), UTF8_STR));
            } catch (UnsupportedEncodingException e) {
                log.error("x-client-token-user内容编码出错:{}", e);
            }
            return chain.filter(exchange.mutate().request(builder.build()).build());
        }
        return unauthorized(exchange);
    }

    /**
     * 网关拒绝，返回401
     *
     * @param
     */
    private Mono<Void> unauthorized(ServerWebExchange serverWebExchange) {
        FuliAssert.unauthorized(401, "当前账号暂无权限，请联系企业管理员开通");
        serverWebExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = serverWebExchange.getResponse().bufferFactory().wrap(HttpStatus.UNAUTHORIZED.getReasonPhrase().getBytes());
        log.info("用户无权限 - 个人鉴权");
        return serverWebExchange.getResponse().writeWith(Flux.just(buffer));
    }

    @Override
    public int getOrder() {
        //数字越小越先执行
        return -500;
    }
}

package com.fuli.server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import static org.springframework.web.cors.CorsConfiguration.ALL;


/**
 * @Author create by XYJ
 * @Date 2019/8/20 17:01
 **/
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final String MAX_AGE = "18000L";

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http
                // Disable default security.
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                // Disable authentication for `/oauth/**` routes.
                .authorizeExchange()
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated()
                .and()
                // 跨域过滤器
//                .addFilterAt(corsFilter(), SecurityWebFiltersOrder.CORS)
                .build();
    }


//    /**
//     * 跨域配置
//     *
//     * @return
//     */
//    public WebFilter corsFilter() {
//        return (ServerWebExchange ctx, WebFilterChain chain) -> {
//            ServerHttpRequest request = ctx.getRequest();
//            if (!CorsUtils.isCorsRequest(request)) {
//                return chain.filter(ctx);
//            }
//            HttpHeaders requestHeaders = request.getHeaders();
//            ServerHttpResponse response = ctx.getResponse();
//            HttpMethod requestMethod = requestHeaders.getAccessControlRequestMethod();
//            HttpHeaders headers = response.getHeaders();
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "");
//            headers.addAll(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, requestHeaders.getAccessControlRequestHeaders());
//            if (requestMethod != null) {
//                headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethod.name());
//            }
//            headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
//            headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, ALL);
//            headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
//            headers.add("P3P","CP=CAOPSA OUR");
//            headers.add("Access-Control-Allow-Headers", ALL);
//            if (request.getMethod() == HttpMethod.OPTIONS) {
//                response.setStatusCode(HttpStatus.OK);
//                return Mono.empty();
//            }
//            return chain.filter(ctx);
//        };
//    }


}

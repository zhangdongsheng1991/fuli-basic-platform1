package com.fuli.server.dynaroute;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.server.dynaroute.model.entity.GatewayRouteDO;
import com.fuli.server.dynaroute.mapper.GatewayRouteMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态路由核心配置类，项目初始化加载数据库的路由配置
 *
 * @Author create by XYJ
 * @Date 2019/9/3 18:01
 **/
@Slf4j
@Service
public class DynamicRouteHandler implements ApplicationEventPublisherAware, CommandLineRunner {
    //自己的获取数据dao
    @Autowired
    private GatewayRouteMapper gatewayRouteMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String  GATEWAY_ROUTES = "gateway:routes";

    @Autowired
    private RedisRouteDefinitionRepository routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    public void run(String... args) {
        this.loadRouteConfig();
    }

    public String loadRouteConfig() {
        QueryWrapper qw = new QueryWrapper();
        List<GatewayRouteDO> gatewayRouteList = gatewayRouteMapper.selectByMap(null);
        //从数据库拿到路由配置
        log.info("网关配置信息：=====>" + JSON.toJSONString(gatewayRouteList));
        redisTemplate.delete(GATEWAY_ROUTES);
        gatewayRouteList.forEach(gatewayRoute -> {
            RouteDefinition definition = new RouteDefinition();
            List<PredicateDefinition> predicates = Lists.newArrayList();
            List<FilterDefinition> filters = Lists.newArrayList();
            definition.setId(gatewayRoute.getRouteName() + ":" + gatewayRoute.getServiceId());
            // 路由条件
            // 路由地址
            PredicateDefinition predicatePath = new PredicateDefinition();
            Map<String, String> predicatePathParams = new HashMap<>(8);
            predicatePath.setName("Path");
            predicatePathParams.put("name", StringUtils.isBlank(gatewayRoute.getRouteName()) ? gatewayRoute.getRouteId().toString() : gatewayRoute.getRouteName());
            predicatePathParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", gatewayRoute.getPath());
            predicatePath.setArgs(predicatePathParams);
            predicates.add(predicatePath);
            // 服务地址
            URI uri = UriComponentsBuilder.fromUriString(StringUtils.isNotBlank(gatewayRoute.getUrl()) ? gatewayRoute.getUrl() : "lb://" + gatewayRoute.getServiceId()).build().toUri();
            // 路由过滤器
            FilterDefinition filterDefinition = new FilterDefinition();
            Map<String, String> filterParams = new HashMap<>(8);
            filterDefinition.setName("StripPrefix");
            // 路径去前缀
            filterParams.put(NameUtils.GENERATED_NAME_PREFIX + "0", "1");
            // 令牌桶流速
            //filterParams.put("redis-rate-limiter.replenishRate", gatewayRoute.getLimiterRate());
            //令牌桶容量
            //filterParams.put("redis-rate-limiter.burstCapacity", gatewayRoute.getLimiterCapacity());
            // 限流策略(#{@BeanName})
            //filterParams.put("key-resolver", "#{@remoteAddrKeyResolver}");
            filterDefinition.setArgs(filterParams);
            filters.add(filterDefinition);
            definition.setPredicates(predicates);
            definition.setFilters(filters);
            definition.setUri(uri);
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        });
        log.info("加载动态路由:{}", gatewayRouteList.size());
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }
    public void deleteRoute(String routeId) {
        routeDefinitionWriter.delete(Mono.just(routeId)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
    }
}

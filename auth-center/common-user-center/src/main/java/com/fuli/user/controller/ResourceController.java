package com.fuli.user.controller;

import com.fuli.auth.common.model.GatherApi;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 收集请求地址
 *
 * @Author create by XYJ
 * @Date 2019/6/14 16:16
 **/
@ApiIgnore
@RequestMapping("/resource")
@RestController
public class ResourceController {


    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(value = "/allApi", method = RequestMethod.GET)
    @ResponseBody
    public List<GatherApi> getAllUrl() {
        // 所有的api集合
        List<GatherApi> apis = new ArrayList<>();
        RequestMappingHandlerMapping mapping  =applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            HandlerMethod method = m.getValue();
            ApiOperation apiOperation = method.getMethod().getAnnotation(ApiOperation.class);
            // 类名
            String name = method.getMethod().getDeclaringClass().getName();
            // 内置方法不收集
            if (name.contains("ApiResourceController") || name.contains("swaggerbootstrapui")|| name.contains("BasicErrorController") || name.contains("ResourceController")){
                continue;
            }
            GatherApi api = new GatherApi();
            // 获取服务名称
            String appName = applicationContext.getEnvironment().getProperty("spring.application.name");
            api.setServiceId(appName);
            api.setClassName(name);
            api.setMethodName(method.getMethod().getName());
            if (apiOperation != null) {
                api.setApiName(apiOperation.value());
            }
            RequestMappingInfo info = m.getKey();
            PatternsRequestCondition p = info.getPatternsCondition();
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                // 请求方式 - GET/POST
                api.setRequestMethod(requestMethod.toString());
            }
            for (String url : p.getPatterns()) {
                // 请求URL
                api.setPath(url);
            }
            // 默认权限
            api.setApiCategory("original");
            api.setApiCode(UUID.randomUUID().toString().replace("-",""));
            api.setIsOpen(1);
            api.setIsAuth(2);
            api.setPriority(1L);
            api.setIsPersist(1);
            api.setStatus(2);
            api.setUpdateTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            api.setCreateTime(DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            apis.add(api);
        }
        return apis;
    }
}

package com.fuli.server.base;

import com.alibaba.fastjson.JSON;
import com.fuli.auth.common.model.GatherApi;
import com.fuli.auth.common.utils.DateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * 收集请求地址
 *
 * @Author create by XYJ
 * @Date 2019/6/14 16:16
 **/
@RequestMapping("/resource")
@RestController
@Slf4j
public class ResourceController {


    @Autowired
    private WebApplicationContext applicationContext;

    @RequestMapping(value = "/allApi", method = RequestMethod.GET)
    @ResponseBody
    public List<GatherApi> getAllUrl() {
//        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        RequestMappingHandlerMapping mapping  =applicationContext.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
        String appName=applicationContext.getEnvironment().getProperty("spring.application.name");

        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        List<GatherApi> apis = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            ApiOperation apiOperation = method.getMethod().getAnnotation(ApiOperation.class);
            PatternsRequestCondition p = info.getPatternsCondition();

            String name = method.getMethod().getDeclaringClass().getName();
            if (name.contains("ApiResourceController") || name.contains("swaggerbootstrapui")|| name.contains("BasicErrorController") || name.contains("ResourceController")){
                continue;
            }

            GatherApi api = new GatherApi();
            api.setServiceId(appName);
            api.setClassName(method.getMethod().getDeclaringClass().getName());
            api.setMethodName(method.getMethod().getName());
            if (apiOperation != null) {
                api.setApiName(apiOperation.value());
            }

            for (String url : p.getPatterns()) {
                api.setPath(url);
            }

            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                api.setRequestMethod(requestMethod.toString());
            }
            ConsumesRequestCondition consumesCondition = info.getConsumesCondition();
            api.setApiCode(UUID.randomUUID().toString().replace("-",""));
            api.setApiCategory("original");
            api.setIsAuth(2);
            api.setIsOpen(1);
            api.setIsPersist(1);
            api.setPriority(1L);
            api.setStatus(2);
            api.setCreateTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            api.setUpdateTime(DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss"));
            apis.add(api);
        }
        log.info("api 采集 ->{}", JSON.toJSON(apis));
        return apis;
    }
}

package com.fuli.logtrace.aop;

import brave.Tracer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fuli.logtrace.annotation.LogTrace;
import com.fuli.logtrace.constant.TraceConstant;
import com.fuli.logtrace.util.IOUtils;
import com.fuli.logtrace.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author create by XYJ
 * @Date 2019/7/3 16:37
 **/
@Aspect   //定义一个切面
@Configuration
public class TraceAspect {

    private static final Logger log = LoggerFactory.getLogger(TraceAspect.class);

    // 定义切点Pointcut
    @Pointcut("@annotation(com.fuli.logtrace.annotation.LogTrace)")
    public void logPoinCut() {
    }

    @Autowired
    private HttpServletRequest request;

    @Around("logPoinCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        try {
            //所有请求参数封装map
            Map<String, Object> reqParams = new HashMap<>();
            reqParams.put("url", request.getRequestURL().toString());
            reqParams.put("reqMethod", request.getMethod());

            //获取所有参数值
            Object[] args = joinPoint.getArgs();
            //从切面织入点处通过反射机制获取织入点处的方法
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            //获取切入点所在的方法
            Method method = methodSignature.getMethod();
            //获取所有参数名称的字符串数组
            String[] parameterNames = methodSignature.getParameterNames();
            //获取注解值操作
            LogTrace myLog = method.getAnnotation(LogTrace.class);
            String apiName = "";
            if (myLog != null) {
                apiName = myLog.value();
                reqParams.put("apiName", apiName);
            }
            //组装所有请求参数
            for (int i = 0; i < args.length; i++) {
                reqParams.put(parameterNames[i], args[i]);
            }
            log.info("{}-接口-{}:{}，请求参数:{}", MDC.get(TraceConstant.X_B3_TRACEId), apiName, request.getRequestURL().toString(), JSON.toJSONString(reqParams));
            // result的值就是被拦截方法的返回值
            Object result = joinPoint.proceed();
            JSONObject re = null;
            if (result != null) {
                if (JsonUtil.objIsJson(result)) {
                    re = JSONObject.parseObject(JSON.toJSONString(result));
                    log.info("{}-接口-{}:{}，返回值:{}", MDC.get(TraceConstant.X_B3_TRACEId), apiName, request.getRequestURL().toString(), re.toJSONString());
                } else {
                    log.info("{}-接口-{}:{}，返回值:{}", MDC.get(TraceConstant.X_B3_TRACEId), apiName, request.getRequestURL().toString(), result.toString());
                }
            }
            return result;
        } catch (Throwable e) {
            //记录本地异常日志
            log.error("异常通知异常");
            log.error("异常信息:{}", e.getMessage());
            return null;
        }
    }

}

package com.fuli.server.base.aop;

import com.alibaba.fastjson.JSON;
import com.fuli.server.base.Result;
import com.fuli.server.base.annotation.NotAllowReCommit;
import com.fuli.server.base.util.IPUtil;
import com.fuli.server.base.util.RedisLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 防表单重复提交切面
 * 基于redis分布式锁实现
 * <p>
 * 1、自定义防止重复提交标记（@NotAllowReCommit）。
 * 2、对需要防止重复提交的Controller里的mapping方法加上该注解。
 * 3、新增Aspect切入点，为@NotAllowReCommit加入切入点。
 * 4、通过redis锁判断在设定时间内是否可以提交数据，
 *
 * @Author create by XYJ
 * @Date 2019/7/8 17:37
 **/
@Aspect
@Component
@EnableAspectJAutoProxy(exposeProxy = true)
@Slf4j
public class NotAllowReCommitAspect {


    @Autowired
    HttpServletRequest request;

    /**
     * @param point
     */
    @Around("@annotation(com.fuli.server.base.annotation.NotAllowReCommit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        //所有请求参数封装map
        Map<String, Object> reqParams = new HashMap<>();
        //获取所有参数值
        Object[] args = point.getArgs();
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        //获取切入点所在的方法
        Method method = methodSignature.getMethod();
        //获取所有参数名称的字符串数组
        String[] parameterNames = methodSignature.getParameterNames();

        //组装所有请求参数
        for (int i = 0; i < args.length; i++) {
            reqParams.put(parameterNames[i], args[i]);
        }
        String ip = IPUtil.getIpAddress(request);
        //获取注解
        //目标类、方法
        String className = method.getDeclaringClass().getName();
        String name = method.getName();
        String ipKey = String.format("%s#%s#%s", className, name,JSON.toJSON(reqParams));
        int hashCode = Math.abs(ipKey.hashCode());
        String lockKey = String.format("%s_%d", ip, hashCode);
        log.info("lockkey:{}",lockKey);
        NotAllowReCommit notAllowReCommit = method.getAnnotation(NotAllowReCommit.class);
        int timeout = notAllowReCommit.timeout();
        if (timeout < 0) {
            timeout = 5;
        }
        //尝试获取锁。获取成功时，可以提交数据
        boolean isSuccess = RedisLockUtil.tryLock(lockKey, 1, timeout);
        if (!isSuccess) {
            return Result.failed("请勿重复提交数据！");
        }
        //执行方法
        Object object = point.proceed();
        return object;
    }


}

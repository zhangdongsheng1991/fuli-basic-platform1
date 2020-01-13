package com.fuli.logtrace.annotation;

import com.fuli.logtrace.aop.TraceAspect;
import com.fuli.logtrace.config.LogTraceFeignSupportConfig;
import com.fuli.logtrace.sleuth.CustomHttpSpanExtractor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author create by XYJ
 * @Date 2019/7/2 16:04
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({LogTraceFeignSupportConfig.class, CustomHttpSpanExtractor.class, TraceAspect.class})
public @interface EnableLogTraceConfig {
}

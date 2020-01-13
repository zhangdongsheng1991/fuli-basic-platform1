package com.fuli.logtrace.annotation;

/**
 * 自定义
 *
 * @Author create by XYJ
 * @Date 2019/7/3 16:31
 **/


import java.lang.annotation.*;
//作用在参数和方法上
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTrace {

    String value() default "";


}

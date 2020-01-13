package com.fuli.server.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表单防重复提交
 *
 * @Author create by XYJ
 * @Date 2019/7/8 17:33
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotAllowReCommit {
    /**
     * 指定时间内不允许提交，单位秒
     *
     * @return
     */
    int timeout() default 5;
}

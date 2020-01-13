package com.fuli.cloud.commons.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 翻看Mybatis-Plus的AbstractWrapper源码，发现构造条件的时候都会使用columnToString方法处理一下column
 * 而QueryWrapper和UpdateWrapper都重写了columnToString方法， 实现非常简单，就是把传进来的column原样返回：
 * 重写columnToString方法，就可以自动把驼峰命名的column转换为下划线命名了！
 * lombok文档，恰好有一个名为@FieldNameContants的注解，可以实现自动把类中的所有属性定义成常量
 *
 * @param <T>
 * @author chenyi
 */
public class QWrapper<T> extends QueryWrapper<T> {

    private static final long serialVersionUID = 8816137900937050311L;

    @Override
    protected String columnToString(String column) {
        // 驼峰命名转换为下划线命名
        return StringUtils.camelToUnderline(column);
    }
}

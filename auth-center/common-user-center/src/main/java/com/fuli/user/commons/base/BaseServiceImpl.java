package com.fuli.user.commons.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @Description:    BaseServiceImpl
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 12:37
 * @Version:        1.0
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T> , T> extends ServiceImpl<M, T> implements BaseService<T> {
    private Class<T> modelClass;

    public BaseServiceImpl() {
        Type type = this.getClass().getGenericSuperclass();
        this.modelClass = (Class)((ParameterizedType)type).getActualTypeArguments()[1];
    }

    @Override
    public boolean batchDelete(@NotEmpty List<Integer> ids) {
        return super.removeByIds(ids);
    }
}


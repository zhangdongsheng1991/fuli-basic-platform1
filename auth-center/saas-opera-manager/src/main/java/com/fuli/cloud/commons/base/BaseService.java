package com.fuli.cloud.commons.base;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description:    BaseService
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 12:37
 * @Version:        1.0
*/
public interface BaseService<T> extends IService<T> {

    /**
     * 批量删除
     * @author      WFZ
     * @param 	    ids : id集合
     * @return      Result
     * @date        2019/7/29 12:37
     */
    boolean batchDelete(@NotEmpty List<Integer> ids);
}

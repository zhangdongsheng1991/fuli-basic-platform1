package com.fuli.cloud.vo;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/7/31
 */
@Data
public class BatchOperateResultVO<Key, FailMsg> {


    @ApiModelProperty("是否全部成功标记")
    private boolean allSuccess = true;

    @ApiModelProperty("每条失败信息")
    private Map<Key, FailMsg> keyToMsg = Maps.newHashMap();

    @ApiModelProperty("成功的key")
    private Set<Key> successKeys = Sets.newHashSet();
}

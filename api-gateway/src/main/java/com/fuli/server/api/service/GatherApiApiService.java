package com.fuli.server.api.service;

import com.fuli.auth.common.model.GatherApi;
import com.fuli.server.api.model.entity.BaseApiDO;

import java.util.List;

/**
 * @Author create by XYJ
 * @Date 2019/8/16 18:21
 **/

public interface GatherApiApiService {


    /**
     * 新增采集的api
     * @param apis
     * @return
     */
    List<BaseApiDO> saveGatherApis(List<GatherApi> apis)throws Exception;


}

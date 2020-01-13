package com.fuli.server.service;

import com.fuli.auth.common.model.GatherApi;
import com.fuli.server.model.entity.BaseApiDO;
import com.fuli.server.model.vo.AuthorityApiVO;

import java.util.List;

/**
 * @Author create by XYJ
 * @Date 2019/8/16 18:21
 **/

public interface AuthorityApiService {
    /**
     * 根据groud查询api列表
     * @param groudId
     * @return
     */
    List<AuthorityApiVO> getApisByGroudId(String groudId);

    /**
     * 权限绑定api
     * @param groupId
     * @param apiIds
     */
    Integer authorityBindApi(String groupId, String[] apiIds,boolean firstRemove);

    /**
     * 新增采集的api
     * @param apis
     * @return
     */
    List<BaseApiDO> saveGatherApis(List<GatherApi> apis)throws Exception;


}

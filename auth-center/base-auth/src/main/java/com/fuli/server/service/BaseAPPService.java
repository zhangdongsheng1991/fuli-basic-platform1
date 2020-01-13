package com.fuli.server.service;

import com.fuli.auth.common.model.CustomClientDetails;
import com.fuli.server.model.dto.BaseAddAppDTO;
import com.fuli.server.model.entity.BaseAppDO;

public interface BaseAPPService {
    /**
     * 根据应用标识获取应用信息
     * @param clientId 应用标识
     * @return
     */
    CustomClientDetails getDetailsByClientId(String clientId);

    /**
     * 新增应用
     *
     * @param app  应用信息
     * @return
     */
    BaseAppDO addApp(BaseAddAppDTO app);

    /**
     * 根据clientid 获取应用基本信息
     * @param clientId
     * @return
     */
    BaseAppDO getInfoByClientId(String clientId);
}

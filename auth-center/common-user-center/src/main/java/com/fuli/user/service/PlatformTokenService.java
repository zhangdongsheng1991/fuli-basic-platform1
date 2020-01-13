package com.fuli.user.service;


import com.fuli.user.dto.PlatformAccessTokenResponseDTO;

/**
 * 中台token服务
 *
 * @author pcg
 * @version 1.0
 * @date 2019-4-16 18:53:23
 */
public interface PlatformTokenService {

    /**
     * 获取中台token
     *
     * @return
     */
    String getPlatformAccessToken();

}

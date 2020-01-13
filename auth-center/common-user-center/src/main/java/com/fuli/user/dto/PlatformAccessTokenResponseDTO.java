package com.fuli.user.dto;

import lombok.Data;

/**
 * 获取中台accessToken响应信息
 * @author pcg
 * @date 2019-4-18 15:02:40
 * @version 1.0
 */
@Data
public class PlatformAccessTokenResponseDTO {
    private int code;
    private AccessTokenDto data;
    private String message;
    private long timestamp;

    @Data
    public class AccessTokenDto {
        /**
         * 接口调用令牌
         */
        private String access_token;
        /**
         * 业务系统刷新应用层access_tok
         */
        private String refresh_token;
        private String clientId;
        /**
         * 业务系统应用层授权的作用域
         */
        private String scope;
        private String token_type;
        private Long expires_in;
        private String jti;
    }
}

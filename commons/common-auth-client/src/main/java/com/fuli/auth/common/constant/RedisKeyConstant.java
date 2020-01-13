package com.fuli.auth.common.constant;

/**
 * @Author create by XYJ
 * @Date 2019/8/16 18:40
 **/

public final class RedisKeyConstant {
    /**
     * 缓存权限组API的key
     */
    public static final String BASE_AUTH_AUTHORITY_API_GROUP="base_auth_authority_api_group_";
    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    public static final String BASE_AUTH_CACHE_CLIENT_KEY="oauth_client_details";
}

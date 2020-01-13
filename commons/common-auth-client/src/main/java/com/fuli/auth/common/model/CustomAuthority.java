package com.fuli.auth.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * 自定义已授权权限标识
 *
 * @Author XYJ
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CustomAuthority implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -45789247874455L;

    /**
     * 权限Id
     */
    private String authorityGroupId;
    /**
     * 权限标识
     */
    private String authority;

    /**
     * 过期时间,用于判断权限是否已过期
     */
    private String expireTime;

    /**
     * api路径
     */
    //    private List<AuthorityApiVO> apis;
    //    private String path;


    @Override
    public String getAuthority() {
        return authority;
    }


}

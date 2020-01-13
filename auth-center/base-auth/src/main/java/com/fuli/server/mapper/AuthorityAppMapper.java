package com.fuli.server.mapper;

import com.fuli.auth.common.model.CustomAuthority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuthorityAppMapper  {

    /**
     *
     * 获取已授权的应用权限
     *
     * @param clientId
     * @return
     */
    List<CustomAuthority> getAuthorityByClientId(@Param("clientId")String clientId);
}

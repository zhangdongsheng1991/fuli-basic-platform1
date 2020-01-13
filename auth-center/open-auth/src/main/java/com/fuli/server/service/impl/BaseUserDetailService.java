package com.fuli.server.service.impl;

import com.fuli.auth.common.model.BaseUser;
import com.fuli.server.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Author create by XYJ
 * @Date 2019/5/7 15:12
 **/
@Slf4j
@Service("baseUserDetailService")
public class BaseUserDetailService implements UserDetailsService {



    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        BaseUser baseUser = new BaseUser();
        baseUser.setUserAccount(name);

        baseUser.setPassword("$2a$10$3y/.JI/nM5bo2G0OoiTZxOQVpIsWQl80gSplb8/qw6dMzVrU.Nee2");

        return baseUser;
    }
}

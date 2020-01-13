package com.fuli.cloud.configuration.service;

import com.fuli.auth.common.model.BaseUser;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.service.UserLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author create by XYJ
 * @Date 2019/5/7 15:12
 **/
@Slf4j
@Service("baseUserDetailService")
public class BaseUserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserLoginService userLoginService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        SystemUserDO user = userLoginService.userLoginInfo(name);
        if (user == null){
            throw new ServiceException(20210002,"用户名或密码错误");
        }
        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(user.getId().toString());
        baseUser.setPhone(user.getPhoneNumber());
        baseUser.setUserAccount(user.getUsername());
        baseUser.setRealName(user.getName());
        baseUser.setPassword(user.getPassword());
        return baseUser;
    }
}

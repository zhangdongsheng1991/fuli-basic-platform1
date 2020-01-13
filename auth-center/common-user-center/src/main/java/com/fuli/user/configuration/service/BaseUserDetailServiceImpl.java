package com.fuli.user.configuration.service;

import com.fuli.auth.common.model.BaseUser;
import com.fuli.user.exception.BusinessException;
import com.fuli.user.service.AppUserLoginAndRegisterService;
import com.fuli.user.vo.AppUserVo;
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
    private AppUserLoginAndRegisterService appUserLoginAndRegisterService;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        AppUserVo user = appUserLoginAndRegisterService.findAppUserByUser(name);
        if (user == null){
            throw new BusinessException("用户名或密码错误");
        }
        BaseUser baseUser = new BaseUser();
        baseUser.setUserId(user.getId());
        baseUser.setEmployeeId(user.getEmployeeId());
        baseUser.setCurrentCompanyId(user.getCompanyId());
        baseUser.setPhone(user.getPhone());
        baseUser.setUserAccount(user.getUsername());
        baseUser.setRealName(user.getRealName());
        baseUser.setCompanyCreditCode(user.getCompanyCreditCode());
        baseUser.setPassword(user.getPassword());
        return baseUser;
    }
}

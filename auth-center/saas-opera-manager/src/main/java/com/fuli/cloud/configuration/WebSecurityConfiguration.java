package com.fuli.cloud.configuration;

import com.fuli.cloud.commons.utils.RsaEncryptUtil;
import com.fuli.cloud.configuration.service.BaseUserDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description:    Web服务配置类
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 17:51
 * @Version:        1.0
 */
@Slf4j
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BaseUserDetailServiceImpl baseUserDetailServiceImpl;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 不定义没有password grant_type
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //使用 MD5 加密
        auth.userDetailsService(baseUserDetailServiceImpl)
                .passwordEncoder(new PasswordEncoder() {
                    @Override
                    public String encode(CharSequence rawPassword) {
                        return (String) rawPassword;
                    }

                    @Override
                    public boolean matches(CharSequence rawPassword, String encodedPassword) {
                        /** 密码解密*/
                        String passwords = RsaEncryptUtil.getPassword(rawPassword.toString());
                        return passwords.equals(encodedPassword);
                    }
                });

        /*auth.userDetailsService(baseUserDetailServiceImpl)
                .passwordEncoder(passwordEncoder);*/
    }

    /**
     * 不拦截URL
     *
     * @param
     * @author WFZ
     * @date 2019/4/16 18:44
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //禁用跨站伪造
        http.csrf().disable();
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                //指定支持基于表单的身份验证。如果未指定FormLoginConfigurer#loginPage(String)，则将生成默认登录页面
                .and().formLogin().permitAll();
    }
}

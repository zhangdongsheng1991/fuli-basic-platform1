package com.fuli.user.service;

import com.fuli.user.commons.PageResult;
import com.fuli.user.commons.Result;
import com.fuli.user.dto.*;
import com.fuli.user.model.AppUser;
import com.fuli.user.vo.AppUserVo;
import com.fuli.user.vo.TokenUserVO;
import com.fuli.user.vo.UserLoginVO;

import java.util.Map;
import java.util.Set;

/**
 * @Description: APP用户service
 * @Author: FZ
 * @CreateDate: 2019/4/15 8:57
 * @Version: 1.0
 */
public interface AppUserLoginAndRegisterService {

    /**
     * 根据用户名获取UserDetails对象 （授权）
     *
     * @param username :用户名
     * @return LoginAppUser
     * @throws
     * @author FZ
     * @date 2019/4/15 9:00
     */
    AppUserVo findAppUserByUser(String username);

    /**
     * SaaS门户登录时获取用户信息
     * @author      WFZ
     * @param 	    phone:手机号
     * @return      Result
     * @date        2019/8/5 16:05
     */
    AppUserVo saAsLoginByPhone(String phone);

    /**
     * SaaS门户登录
     * @author      WFZ
     * @param 	    userLoginDTO
     * @return      Result
     * @date        2019/8/5 16:05
     */
    Result saAsLogin(UserLoginDTO userLoginDTO);

    /**
     * 中台获取token
     * @author      fengjing
     * @param       id：用户id
     * @param       companyId：企业id
     * @return      AppUserVo
     * @date        2019/6/17 16:05
     */
    AppUserVo getUserNews(String id, String companyId);

    /**
     * APP用户注册
     *
     * @param userRegisterDTO
     * @return Result
     * @author FZ
     * @date 2019/4/15 19:03
     */
    Result appUserRegister(UserRegisterDTO userRegisterDTO);

    /**
     * APP用户登录
     *
     * @param userLoginVo:用户登录VO
     * @return Result
     * @author FZ
     * @date 2019/4/15 9:35
     */
    Result appUserLogin(UserLoginVO userLoginVo);


    /**
     * 根据openID获取主键唯一ID
     * @author      fengjing
     * @param       openId
     * @return
     * @date        2019/5/11 14:47
     */
    Result obtainPrimaryKey(String openId);

    /**
     * @Description:(分页查询用户手机号)
     * @author      fengjing
     * @date        2019/5/11 14:52
     */
    PageResult pageUserPhone(Map<String, Object> map);

   /**
    * 中台用户同步接口
    * @author      fengjing
    * @param 	   appUserDTO
    * @return      Result
    * @date        2019/8/12 15:09
    */
    Result platformUserSynchronized(AppUserDTO appUserDTO);

    /**
     * 订阅中台用户修改
     * @author      fengjing
     * @param       dto:
     * @return      Result
     * @date        2019/5/16 20:02
     */
    Result platformUpdate(PlatformUpdateDTO dto);


    /**
     * 设置/手势密码
     * @author      fengjing
     * @param       dto
     * @param       userVO
     * @return
     * @date        2019/6/3 17:38
     */
    Result gesturePassword(ChangeGesturePwdDTO dto , TokenUserVO userVO);


    /**
     * 重置用户密码
     * @param resetPasswordDTO 用户id
     * @return
     */
    Result resetPassword(ResetPasswordDTO resetPasswordDTO);


    /**
     * 通过账号批量获取用户id
     * @param accounts 账号
     * @return
     */
    Map<String,Long> getAccounts(Set<String> accounts);
}

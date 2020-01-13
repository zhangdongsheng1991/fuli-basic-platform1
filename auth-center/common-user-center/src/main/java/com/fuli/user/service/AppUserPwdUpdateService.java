package com.fuli.user.service;

import com.fuli.user.commons.Result;
import com.fuli.user.dto.AppUserUpdatePasswordRequest;
import com.fuli.user.dto.AppUserUpdatePayPwdRequest;
import com.fuli.user.dto.IousRequestDTO;
import com.fuli.user.dto.SmsSendDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.vo.TokenUserVO;

import java.util.Map;

/**
 * @Deprecated 用户密码修改服务
 * @Author xq
 * @Date 2019/4/16
 * @Version 1.0
 */
public interface AppUserPwdUpdateService {

    /**
     * 用户查询
     *
     * @param phone 用户信息
     * @return 修改影响行
     */
    AppUser findAppUserByPhone(String phone);

    /**
     * 修改本地用户密码
     *
     * @param request 请求参数封装
     * @param type 3:忘记密码 ，2- 修改密码
     * @return 修改用户
     */
    Result updatePasswordByPhone(AppUserUpdatePasswordRequest request, int type);

    /**
     * 修改用户本地支付密码
     *
     * @param request 请求参数封装
     * @param tokenUserVO : 当前登录用户
     * @return 修改影响行
     */
    Result updatePayPasswordByPhone(AppUserUpdatePayPwdRequest request ,TokenUserVO tokenUserVO);


    /**
     * 发送验证码
     * @author      xq
     * @param       smsSendDto 手机验证码发送封装
     * @param       phone
     * @return
     */
    Result sendSms(SmsSendDTO smsSendDto, String phone);

    /**
     * 中台系统修改本系统用户密码时同步
     * @author      fengjing
     * @param       map -- 用户id，密码
     * @return      Result
     * @date        2019/6/6 10:43
     */
    Result syncPassword(Map<String, Object> map);

    /**
     * 登录密码校验
     * @author      fengjing
     * @param       map -- password : 密码
     * @param       tokenUserVO : 登录用户信息
     * @return      Result
     * @date        2019/6/11 9:26
     */
    Result logonPwdValidity(Map<String, Object> map , TokenUserVO tokenUserVO);



    /**
     * 根据companyOpenId,userOpenId返回用户信息
     * @author      fengjing
     * @param       dto : companyOpenId,userOpenId
     * @return      Result
     * @date        2019/5/28 14:38
     */
    Result companyUserData(IousRequestDTO dto);

    /**
     * 支付密码校验
     * @author      fengjing
     * @param       dto : openId不为空， 根据openId校验
     * @return      Result
     * @date        2019/5/6 16:59
     */
    Result checkPayPwd(IousRequestDTO dto);

}

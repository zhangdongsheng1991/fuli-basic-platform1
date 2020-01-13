package com.fuli.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.user.commons.Result;
import com.fuli.user.dto.ChangePhoneDTO;
import com.fuli.user.dto.UserCertificationDTO;
import com.fuli.user.dto.UserSwitchCompanyDTO;
import com.fuli.user.model.AppUser;
import com.fuli.user.vo.AppUserIdVO;
import com.fuli.user.vo.AppUserIdsVO;
import com.fuli.user.vo.PageResultVO;
import com.fuli.user.vo.TokenUserVO;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: TODO
 * @Author: WS
 * @CreateDate: 2019/4/18 19:19
 * @Version: 1.0
 */
public interface AppUserInfoService extends IService<AppUser> {

    /**
     * app用户获取用户信息，带企业信息  2019-08-07 修改
     * @author      WFZ
     * @param 	    id : 用户id
     * @return      Result
     * @date        2019/8/7 14:57
     */
    Result getUserInfo(Long id);

    /**
     * 用户切换企业，重新生成token
     * @author      fengjing
     * @param       request : 请求类
     * @param       userInfoVO  : 登录用户信息
     * @return      Result
     * @date        2019/6/21 11:21
     */
    Result switchCompany(UserSwitchCompanyDTO request, TokenUserVO userInfoVO);

    /**
     * 实名校验，并修改实名状态
     * @author      WFZ
     * @param 	    dto ：请求类
     * @return      Result
     * @date        2019/8/7 16:24
     */
    Result updateRealNameStatus(UserCertificationDTO dto);

    /**
     * 更换手机号 - 2019-08-02 修改接口
     * @author      WFZ
     * @param 	    changePhoneDto  : 请求类
     * @param 	    userInfoVO  : 登录用户信息
     * @return      Result
     * @date        2019/8/2 16:32
     */
    Result replacePhone(ChangePhoneDTO changePhoneDto, TokenUserVO userInfoVO);

    /**
     *获取用户id列表
     * @param currentPage 当前页码
     * @param pageSize 页大小
     * @return
     */
    PageResultVO<List<AppUserIdsVO>> getUserIds(Integer currentPage, Integer pageSize);
    /**
     * @Description:(批量手机号查询批量id)
     * @author      fengjing
     * @date        2019/6/27 9:34
     */
    List<AppUserIdVO> phoneGetUserId(List<String> list);

    /**
     * 根据身份证号获取用户信息
     * @author      WFZ
     * @param 	    idCard ： 身份证号
     * @return      Result
     * @date        2019/8/8 10:14
     */
    AppUser findByIdCard(String idCard);

    /**
     * 根据身份证号判断用户是否实名
     * @author      WFZ
     * @param 	    idCard ： 身份证号码
     * @return      Result
     * @date        2019/8/12 10:36
     */
    Boolean isRealNameByIdCard(String idCard);

}

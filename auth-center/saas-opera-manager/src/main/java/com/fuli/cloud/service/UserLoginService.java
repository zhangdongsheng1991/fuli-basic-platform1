package com.fuli.cloud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.user.UserLoginDTO;
import com.fuli.cloud.dto.user.UserChangePasswordDTO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;

/**
 * @Description:    用户登录
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 11:34
 * @Version:        1.0
*/
public interface UserLoginService extends IService<SystemUserDO> {

    /**
     * 用户登录
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/7/29 11:33
     */
    Result userLogin(UserLoginDTO dto);

    /**
     * 认证服务生成token用
     * @author      WFZ
     * @param 	    userAccount : 账户或手机号
     * @return      Result
     * @date        2019/7/31 20:08
     */
    SystemUserDO userLoginInfo(String userAccount);

    /**
     * 我的头像框
     * @author      WFZ
     * @param 	    user : 登录用户信息
     * @return      Result
     * @date        2019/8/1 14:55
     */
    Result pictureFrame(TokenUser user);

    /**
     * 我的档案
     * @author      WFZ
     * @param 	    user : 登录用户信息
     * @return      Result
     * @date        2019/8/1 14:55
     */
    Result findUserArchivesVO(TokenUser user);

    /**
     * 修改初始密码
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/9/29 10:16
     */
    Result updateInitPwd(UserChangePasswordDTO dto);
}

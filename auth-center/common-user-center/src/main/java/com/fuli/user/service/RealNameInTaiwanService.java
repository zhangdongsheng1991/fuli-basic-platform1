package com.fuli.user.service;

import com.fuli.user.commons.Result;
import com.fuli.user.dto.PlatformPasswordDTO;
import com.fuli.user.dto.RealNameInTaiwanDTO;

/**
 * @author fengjing
 * @version V1.0
 * @Description: (中台实名认证service接口)
 * @date 2019/4/17 15:40
 */
public interface RealNameInTaiwanService {


    /**
     * 中台实名三要素认证
     * @author fengjing
     * @param  dto
     * @return Result
     * @date 2019/4/17 15:42
     */
    Result obtainRealName(RealNameInTaiwanDTO dto);

    /**
     * 非本地用户登录
     * @author      WFZ
     * @param 	    username
     * @param 	    password
     * @return      Result
     * @date        2019/10/17 11:34
     */
    Result thirdLogin(String username, String password);
    /**
     * 同步修改中台密码
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/10/17 11:09
     */
    Result syncPlatformPwd( PlatformPasswordDTO dto);

    /**
     * 非本地用户修改密码去中台校验原密码
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/10/17 10:14
     */
    Result checkPwd( PlatformPasswordDTO dto);

    /**
     * 非本地用户修改密码去中台校验原密码
     * @author      WFZ
     * @param 	    dto
     * @return      Result
     * @date        2019/10/17 10:14
     */
    Result registerLogin( PlatformPasswordDTO dto);
}

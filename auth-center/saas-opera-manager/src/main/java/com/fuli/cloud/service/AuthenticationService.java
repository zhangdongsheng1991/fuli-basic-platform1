package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.model.SystemMenuDO;
import com.fuli.cloud.model.TokenUser;

import java.io.Serializable;
import java.util.List;


/**
 * @Description:    菜单接口
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 10:37
 * @Version:        1.0
*/
public interface AuthenticationService {

    /**
     * url鉴权
     * @author      WFZ
     * @param 	    userId : 用户id
     * @param 	    url : 请求URL
     * @return      Result
     * @date        2019/6/26 10:29
     */
    Result<Boolean> requestUrl(String userId, String url);

}

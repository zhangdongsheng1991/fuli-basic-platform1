package com.fuli.user.feign;

import com.fuli.user.commons.Result;
import com.fuli.user.dto.UserMsgDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @Description: (用户消息设置)
* @author fengjing
* @date 2019/5/5 9:34
* @version V1.0
*/
@FeignClient("app-basic-server")
public interface AppUserMsgSetFeign {
    /**
     * 用户消息设置
     * @param       userMsgDTO
     * @author      fengjing
     * @return      Result
     * @date        2019/5/5 9:34
     */
    @PostMapping(value = "/setting/initUpdateMsgPush")
    Result userMessageSettings(@RequestBody UserMsgDTO userMsgDTO);
}

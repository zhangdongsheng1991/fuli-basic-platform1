package com.fuli.user.feign;

import com.fuli.user.commons.Result;
import com.fuli.user.vo.PushVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
* @Description: (友盟)
* @author fengjing
* @date 2019/5/21 18:23
* @version V1.0
*/
@FeignClient("app-message-upush")
public interface UpushMessageFetFeign {

    @RequestMapping(value = "/app_push/sendMsgUniCast",method = RequestMethod.POST)
    Result sendMsgUniCast(@RequestBody PushVO pushVo);

}

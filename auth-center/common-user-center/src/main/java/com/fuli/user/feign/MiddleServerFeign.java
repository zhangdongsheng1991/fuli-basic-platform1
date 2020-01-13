package com.fuli.user.feign;

import com.fuli.user.commons.Result;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @Description: (调取中台Feign)
* @author fengjing
* @date 2019/5/17 15:25
* @version V1.0
*/
@FeignClient("basic-middle-server")
public interface MiddleServerFeign {
    /**
     * 用户登录去中台验证
     * @param userName
     * @param password
     * @return Result
     */
    @RequestMapping(value = "/api-platform/thirdLogin",method = RequestMethod.POST)
    Result thirdLogin(@RequestParam("username") String userName ,@RequestParam("password") String password);

    /**
     * 用户数据补偿
     * @author      WFZ
     * @param 	    userId
     * @return      Result
     * @date        2019/10/17 11:57
     */
    @PostMapping("/api-approval/findOtherUserID")
    Result findOtherUserID(@ApiParam(required = true,name = "userId",value = "业务系统用户id")@RequestParam String userId);
    /**
     * 账号密码同步接口
     * @author      fengjing
     * @param       map
     * @return      Result
     * @date        2019/5/20 15:02
     */
    /*@RequestMapping(value = "/api-user/registerLogin",method = RequestMethod.POST)
    Result registerLogin(@RequestBody Map<Object, Object> map);*/


    /**
     * 用户实名认证后同步到中台
     * @author      WFZ
     * @param       appUserDO
     * @return      Result
     * @date        2019/8/30 10:46
     */
    /*@PostMapping(value = "/sync/sendUserToMiddle")
    Result sendUserToMiddle(@RequestBody AppUser appUserDO);*/

    /**
     * 修改手机号同步中台
     * @author      WFZ
     * @param       map
     * @return      Result
     * @date        2019/10/9 20:05
     */
    /*@PostMapping(value = "/api-platform/modifyPhoneNumber")
    Result modifyPhoneNumber(@RequestBody Map map);*/
}

package com.fuli.cloud.feign;


import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.service.OpenServiceDTO;
import com.fuli.cloud.dto.user.SmsSendDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * @Description:    basic-server 服务feign
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 11:27
 * @Version:        1.0
*/
@FeignClient("basic-server")
public interface BasicServerFeign {

    /**
     * 图片验证码校验
     * @author      fengjing
     * @param 	    imageCode
     * @return      Result
     * @date        2019/7/9 19:55
     */
    @PostMapping(value = "/verifyCode/verification")
    Result verificationImageCode(@RequestParam("imageCode") String imageCode);

    /**
     * 开通权限模块
     * @author      WFZ
     * @param 	    openModuleDTO
     * @return      Result
     * @date        2019/7/30 19:20
     */
    @PostMapping(value = "/companyModule/openUp")
    Result openUp(@RequestBody OpenServiceDTO openModuleDTO);

    /**
     * 发送手机验证码
     *
     * @param smsSendDto 手机验证码封装类
     * @return Result
     */
    @PostMapping(value = "/sms/sendSmsVerifyCode")
    Result sendSmsVerifyCode(@RequestBody SmsSendDTO smsSendDto);

    /**
     * 根据企业id获取企业
     * @author      WFZ
     * @param       companyId
     * @return      Result
     * @date        2019/8/27 18:14
     */
    @PostMapping(value = "/inner/getCompanyById")
    Result getCompanyById(@RequestParam("companyId") Long companyId);
}

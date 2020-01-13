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
 * @Description:    salary 服务feign  薪发放
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 11:27
 * @Version:        1.0
*/
@FeignClient("salary")
public interface SalaryFeign {

    /**
     * saas通知-企业服务通知接口（通知开启或者关闭扣减业务） 通知薪发放
     * @author      WFZ
     * @param 	    map
     * @return      Result
     * @date        2019/8/27 19:55
     */
    @PostMapping(value = "/CorpConsumChannel/noticeAddConsum")
    Result noticeAddConsum(@RequestBody Map map);

}

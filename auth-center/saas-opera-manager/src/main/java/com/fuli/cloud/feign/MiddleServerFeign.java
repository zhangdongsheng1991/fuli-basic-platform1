package com.fuli.cloud.feign;

import com.fuli.cloud.commons.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description:    basic-middle-server 服务
 * @Author:         WFZ
 * @CreateDate:     2019/9/5 17:46
 * @Version:        1.0
*/
@FeignClient("basic-middle-server")
public interface MiddleServerFeign {

    /**
     * 为开通薪发放的企业新增一个空审批流
     * @author      WFZ
     * @param 	    companyId : 企业id
     * @return      Result
     * @date        2019/9/5 17:46
     */
    @PostMapping("/saas-approval/addApprovalForXFF")
    Result addApprovalForXFF(@RequestParam("companyId") Long companyId);
}

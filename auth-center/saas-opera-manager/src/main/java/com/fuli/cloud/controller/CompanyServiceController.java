package com.fuli.cloud.controller;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.utils.PublicUtil;
import com.fuli.cloud.constant.OperateConstant;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.dto.service.OpenServiceDTO;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.service.CompanyMsgService;
import com.fuli.cloud.service.CompanyServiceService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Description:    企业开通服务相关
 * @Author:         WFZ
 * @CreateDate:     2019/7/30 10:49
 * @Version:        1.0
*/
@Slf4j
@RestController
@RequestMapping("/service")
@Api(tags="企业开通服务相关 --温福州")
public class CompanyServiceController extends BaseController {

    @Autowired
    CompanyServiceService companyServiceService;

    private static final String COMPANY_ID="companyId";

    @PostMapping("/listService")
    @ApiOperation("企业开通服务列表--温福州")
    @DynamicParameters(name = "map",properties = {
            @DynamicParameter(name = COMPANY_ID,value = "企业id",required = true)
    })
    public Result listService(@RequestBody Map map){
        log.info("查询企业开通的服务列表入参:{}",map);
        return companyServiceService.listServiceModule(map.get(COMPANY_ID).toString());
    }


    @PostMapping("/onOrOff")
    @ApiOperation("企业开通或关闭服务--温福州")
    public Result onOrOff(@Validated @RequestBody OpenServiceDTO openServiceDTO){
        log.info("企业开通或关闭服务入参:{}",openServiceDTO.toString());
        return companyServiceService.onOrOffService(openServiceDTO);
    }


    @PostMapping("/listCompanyEnableService")
    @ApiOperation("查询企业开通的有效服务")
    public Result listCompanyEnableService(@RequestBody Map map){
        log.info("查询企业开通的有效服务入参:{}",map);
        return companyServiceService.listCompanyEnableService(map.get(COMPANY_ID).toString());
    }

}

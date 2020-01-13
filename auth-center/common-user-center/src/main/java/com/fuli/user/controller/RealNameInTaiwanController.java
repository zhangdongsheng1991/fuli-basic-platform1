package com.fuli.user.controller;

import com.fuli.user.commons.Result;
import com.fuli.user.dto.RealNameInTaiwanDTO;
import com.fuli.user.service.RealNameInTaiwanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
* @Description: (中台实名认证controller)
* @author fengjing
* @date 2019/4/16 15:47
* @version V1.0
*/
@RestController
@Api(tags = "实名认证")
@Slf4j
public class RealNameInTaiwanController {

    @Autowired
    private RealNameInTaiwanService realNameInTaiwanServiceImpl;

    /**
     * @Description:(中台实名三要素认证)
     * @author      fengjing
     * No such property: code for class: Script1
     * @return      JSONObject
     * @date        2019/4/16 15:56
     */
    @ApiOperation(value = "获取中台实名认证")
    @PostMapping("/feal")
    public Result obtainRealName(@RequestBody RealNameInTaiwanDTO realNameInTaiwanDto){
        log.info("实名认证接收入参：{}",realNameInTaiwanDto.toString());
        return realNameInTaiwanServiceImpl.obtainRealName(realNameInTaiwanDto);
    }
}

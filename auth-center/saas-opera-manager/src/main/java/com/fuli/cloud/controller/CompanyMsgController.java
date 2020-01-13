package com.fuli.cloud.controller;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.constant.OperateConstant;
import com.fuli.cloud.dto.CompMsgDealDto;
import com.fuli.cloud.dto.CompanyMessageDto;
import com.fuli.cloud.model.CompanyMessageDO;
import com.fuli.cloud.service.CompanyMsgService;
import com.fuli.cloud.vo.CompanyMessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/operation")
@RestController
@Slf4j
@Api(tags="企业留言api")
public class CompanyMsgController extends BaseController {

    @Autowired
    CompanyMsgService companyMsgService;

    @ApiOperation("查询企业留言列表信息  杨红梅")
    @GetMapping("listCompanyMsg")
    public Result<PageResult<CompanyMessageVO>> listCompanyMsg(CompanyMessageDto companyMessageDto){
        return companyMsgService.getCompanyMsgInfo(companyMessageDto);
    }

    @ApiOperation("查询企业留言详情  杨红梅")
    @GetMapping("getCompanyMsg")
    public Result<CompanyMessageVO> getCompanyMsg(@ApiParam(name="id",value = "企业留言ID",required = true) @RequestParam("id") Long id){
        CompanyMessageVO companyMessageVO = companyMsgService.getCompanyMsgDetail(id);
        return Result.succeed(companyMessageVO);
    }

    @ApiOperation("处理企业留言  杨红梅")
    @PostMapping("dealCompanyMsg")
    public Result dealCompanyMsg(@RequestBody @Validated CompMsgDealDto companyMessageDto){
        //留言ID
        Long msgId = companyMessageDto.getId();
        CompanyMessageDO companyMessageDO = new CompanyMessageDO();
        companyMessageDO.setId(msgId);
        companyMessageDO.setState(companyMessageDto.getState());
        companyMessageDO.setDealUser(getUserName());
        companyMessageDO.setRemark(companyMessageDto.getRemark());
        return companyMsgService.dealCompanyMsg(companyMessageDO);
    }

//    @ApiOperation("无效反馈企业留言 杨红梅")
//    @PostMapping("invalidCompanyMsg")
//    public Result invalidCompanyMsg(@RequestBody CompMsgDealDto companyMessageDto){
//        //留言ID
//        Long msgId = companyMessageDto.getId();
//        if(msgId == null){
//            return Result.failed(CodeEnum.SELECT_IS_EMPTY);
//        }
//        //获取当前用户id
////        Integer userId = getSystemUserId();
//        CompanyMessageDO companyMessageDO = new CompanyMessageDO();
//        companyMessageDO.setId(msgId);
//        companyMessageDO.setState(OperateConstant.MESSAGE_STATE_INVALID);
//        companyMessageDO.setDealUser(getUserName());
//        companyMessageDO.setRemark(companyMessageDto.getRemark());
//        return companyMsgService.dealCompanyMsg(companyMessageDO);
//    }
}

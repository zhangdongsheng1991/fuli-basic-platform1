package com.fuli.cloud.controller;

import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.service.SystemUserGrantCompanyService;
import com.fuli.cloud.vo.ListByUserIdGrantCompanyVO;
import com.fuli.cloud.vo.ListUserGrantByCompanyIdVO;
import com.fuli.cloud.vo.SystemUserGrantCompanyVo;
import com.fuli.logtrace.annotation.LogTrace;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author chenyi
 * @date 2019/12/4
 */
@Slf4j
@RequestMapping("/systemUserGrantCompany")
@RestController
@Api(tags="用户管理的企业分配API")
public class SystemUserGrantCompanyController extends BaseController {

    @Autowired
    SystemUserGrantCompanyService systemUserGrantCompanyService;

    @PostMapping("/listGrantCompany")
    @ApiOperation("查询数据权限列表")
    public Result<PageResult<SystemUserGrantCompanyVo>> listGrantCompany(@RequestBody SystemUserGrantCompanyQryDto qryDto){
        return systemUserGrantCompanyService.getGrantCompanyInfo(qryDto);
    }


    @ApiOperation("移除授权|批量移除授权")
    @PostMapping("/removeAuth")
    public Result removeAuth(@Validated @RequestBody RemoveAuthDTO removeAuthDTO) {

        systemUserGrantCompanyService.removeByIds(removeAuthDTO.getIds());

        return Result.succeed();
    }

    @PostMapping("/add")
    @ApiOperation("新增企业管理人（授权）")
    public Result add(@Validated  @RequestBody AddSystemUserGrantCompanyDTO addSystemUserGrantCompanyDTO){
        return systemUserGrantCompanyService.addGrantCompany(addSystemUserGrantCompanyDTO,getSystemUser());
    }

    @PostMapping("/update")
    @ApiOperation("编辑企业管理人（编辑授权）")
    public Result update(@Validated @RequestBody UpdateSystemUserGrantCompanyDTO updateSystemUserGrantCompanyDTO){
        return systemUserGrantCompanyService.updateGrantCompany(updateSystemUserGrantCompanyDTO,getSystemUser());
    }

    @PostMapping("/listUserGrantByCompanyId")
    @ApiOperation("根据企业id获取已添加的管理人员列表")
    public Result<ListUserGrantByCompanyIdVO> listUserGrantByCompanyId(@Validated @RequestBody ListUserGrantByCompanyIdDTO listUserGrantByCompanyIdDTO){
        return systemUserGrantCompanyService.listUserGrantByCompanyId(listUserGrantByCompanyIdDTO);
    }

    @PostMapping("/listByUserId")
    @ApiOperation("根据用户id获取我管理的企业列表")
    public Result<PageResult<ListByUserIdGrantCompanyVO>> listByUserId(@Validated @RequestBody ListCompanyManagerByUserIdDTO listCompanyManagerByUserIdDTO){
        log.info("根据用户id获取我管理的企业列表入参：{}",listCompanyManagerByUserIdDTO);
        return systemUserGrantCompanyService.listByUserId(listCompanyManagerByUserIdDTO);
    }
}

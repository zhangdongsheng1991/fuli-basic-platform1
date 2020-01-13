package com.fuli.cloud.controller;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.BaseIdDTO;
import com.fuli.cloud.dto.quick.QuickModuleBatchInsertDTO;
import com.fuli.cloud.dto.quick.QuickModuleBatchSortDTO;
import com.fuli.cloud.model.SystemQuickModuleDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemQuickModuleService;
import com.fuli.cloud.vo.SystemModuleVO;
import com.fuli.cloud.vo.menu.MenuButtonVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;


/**
 * @Description:    快捷功能管理控制层
 * @Author:         FZ
 * @CreateDate:     2019/4/26 14:06
 * @Version:        1.0
 */
@Slf4j
@RestController
@RequestMapping("/quick")
@Api(tags = "快捷功能管理(工作台) --温福州")
public class SystemQuickModuleController extends BaseController {

    @Autowired
    SystemQuickModuleService systemQuickModuleService;

    @PostMapping(value = "listMyWorkbench")
    @ApiOperation(value = "获取我的工作台快捷功能 --温福州")
    public Result<List<MenuButtonVO>> listMyWorkbench() {
        return systemQuickModuleService.listAddedByUserId(getSystemUser(),1);
    }



    @PostMapping(value = CommonConstant.KEY_LIST)
    @ApiOperation(value = "获取数据列表（功能库）--温福州")
    public Result<List<SystemModuleVO>> list() {
        return systemQuickModuleService.list(getSystemUser());
    }


    @PostMapping(value = "listAdded")
    @ApiOperation(value = "获取已添加的快捷功能 --温福州")
    public Result<List<MenuButtonVO>> listAdded() {
        return systemQuickModuleService.listAddedByUserId(getSystemUser(),2);
    }

    @PostMapping(value = "/wipeData")
    @ApiOperation(value = "清空数据 --温福州")
    public Result wipeData() {
        return systemQuickModuleService.wipeData(getSystemUser());
    }

    @PostMapping(value = CommonConstant.KEY_ADD)
    @ApiOperation(value = "新增 --温福州")
    public Result add(@Validated @RequestBody SystemQuickModuleDO systemQuickModuleDO) {
        TokenUser userInfo = getSystemUser();
        systemQuickModuleDO.setUserId(userInfo.getId());
        return systemQuickModuleService.add(systemQuickModuleDO);
    }

    @PostMapping(value = CommonConstant.KEY_DELETE)
    @ApiOperation(value = "删除 --温福州")
    public Result delete(@Validated @RequestBody BaseIdDTO baseIdDTO) {
        return Result.status(systemQuickModuleService.removeById(baseIdDTO.getId()));
    }

    /**
     * 自定义排序
     * @author      WFZ
     * @param 	    quickModuleDto
     * @return      Result
     * @date        2019/7/15 18:35
     */
    @PostMapping("/batchSort")
    @ApiOperation(value = "批量排序--温福州")
    public Result batchSort(@Validated @RequestBody QuickModuleBatchSortDTO quickModuleDto) {
        return systemQuickModuleService.batchSort(quickModuleDto,getSystemUser());
    }

    @ApiIgnore
    @PostMapping("/batchInsert")
    @ApiOperation(value = "批量新增--温福州")
    public Result batchInsert(@RequestBody @Validated QuickModuleBatchInsertDTO dto) {
        return systemQuickModuleService.batchInsert(getSystemUser(), dto.getList());
    }

}

package com.fuli.cloud.controller;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.BaseIdDTO;
import com.fuli.cloud.dto.PositionAddDto;
import com.fuli.cloud.dto.PositionDto;
import com.fuli.cloud.dto.PositionUpdateDto;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.PositionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/position")
@RestController
@Slf4j
@Api(tags="岗位相关api")
public class PositionController extends BaseController {

    @Autowired
    PositionService positionService;

    @ApiOperation("查询岗位列表  杨红梅")
    @PostMapping("listPosition")
    public Result<PageResult<PositionVo>> listDepartment(@RequestBody PositionDto positionDto){
        log.info("岗位列表查询参数：" + positionDto);
        return positionService.getPositionList(positionDto);
    }

    @ApiOperation("新增岗位  杨红梅")
    @PostMapping("addPosition")
    public Result addPosition(@Validated @RequestBody PositionAddDto positionAddDto){
        log.info("新增岗位参数："+ positionAddDto);
        SystemPosition position = new SystemPosition();
        BeanUtils.copyProperties(positionAddDto, position);
        //获取操作人
        String username = getUserName();
        log.info("操作人："+username);
        position.setOperationAccount(username);
        return positionService.addPosition(position);
    }

    @ApiOperation("修改岗位  杨红梅")
    @PostMapping("updatePosition")
    public Result updatePosition(@Validated @RequestBody PositionUpdateDto positionUpdateDto){
        log.info("新增岗位参数："+ positionUpdateDto);
        SystemPosition position = new SystemPosition();
        BeanUtils.copyProperties(positionUpdateDto,position);
        //获取操作人
        String username = getUserName();
        log.info("操作人："+username);
        position.setOperationAccount(username);
        return positionService.updatePosition(position);
    }

    @ApiOperation("查询岗位详情（入参为岗位ID）  杨红梅")
    @PostMapping("getPositionDetail")
    public Result<PositionVo> getPositionDetail(@RequestBody BaseIdDTO baseIdDto){
        String id = baseIdDto.getId();
        if(StringUtils.isBlank(id)){
            throw new CustomException(CodeEnum.POSITION_ID_IS_NULL);
        }
        PositionVo positionVo = positionService.getPositionAndParentById(id);
        return Result.succeed(positionVo);
    }

    @ApiOperation("启用岗位（入参为岗位ID）  杨红梅")
    @PostMapping("enablePosition")
    public Result enablePosition(@RequestBody BaseIdDTO baseIdDto){
        log.info("启用岗位参数：" + baseIdDto);
        String id = baseIdDto.getId();
        if(StringUtils.isBlank(id)){
            throw new CustomException(CodeEnum.POSITION_ID_IS_NULL);
        }
        String username = getUserName();
        log.info("操作人："+username);
        return positionService.updatePositionStatus(id, CommonConstant.STATUS_ENABLE,username);
    }

    @ApiOperation("禁用岗位(入参为岗位ID)  杨红梅")
    @PostMapping("disablePosition")
    public Result disablePosition(@RequestBody BaseIdDTO baseIdDto){
        log.info("禁用岗位参数：" + baseIdDto);
        String id = baseIdDto.getId();
        if(StringUtils.isBlank(id)){
            throw new CustomException(CodeEnum.POSITION_ID_IS_NULL);
        }
        //获取操作人
        String username = getUserName();
        log.info("操作人："+username);
        return positionService.updatePositionStatus(id,CommonConstant.STATUS_DISABLE,username);
    }

    @ApiOperation("获取树状结构岗位信息(入参为部门ID)  杨红梅")
    @PostMapping("getPositionTree")
    public Result<List<OrganazationVo> > getPositionTree(@RequestBody BaseIdDTO baseIdDto){
        //部门id
        String id = baseIdDto.getId();
        if(StringUtils.isBlank(id)){
            throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
        }
        List<OrganazationVo> positionList = positionService.getDeptPositionTree(id,0,2,null);
        return Result.succeed(positionList);
    }

    @ApiOperation("获取上级岗位树(入参为部门ID、岗位ID)  杨红梅")
    @PostMapping("getParentPositionTree")
    public Result<List<OrganazationVo> > getParentPositionTree(@RequestBody PositionDto positionDto){
        //部门id
        if(positionDto.getDepartmentId() == null){
            throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
        }
        //岗位id
        if(positionDto.getId() == null){
            throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
        }

        List<OrganazationVo> positionList = positionService.getParentPositionTree(positionDto.getDepartmentId(),positionDto.getId());
        return Result.succeed(positionList);
    }
}

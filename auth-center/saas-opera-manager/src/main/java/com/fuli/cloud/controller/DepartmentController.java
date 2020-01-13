package com.fuli.cloud.controller;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.utils.JacksonUtil;
import com.fuli.cloud.commons.utils.ListHeaderUtil;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.service.DepartmentService;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.vo.DepartmentVo;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.employee.EmpDeptPositionVo;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.DynamicParameter;
import io.swagger.annotations.DynamicParameters;
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
import java.util.Map;

/**
 * @Description:    部门相关
 * @Author:         yhm
 * @CreateDate:     2019/7/9 14:13
 * @Version:        1.0
*/
@RequestMapping("/department")
@RestController
@Api(tags="部门相关api")
@Slf4j
public class DepartmentController extends BaseController {

    @Autowired
	DepartmentService departmentService;
    @Autowired
    PositionService positionService;


	@ApiOperation("部门、岗位、角色名称唯一性校验  杨红梅")
	@PostMapping("checkUniqueness")
	public Result checkUniqueness(@Validated @RequestBody CheckUniquenessDTO checkUniquenessDTO){
		log.info("部门、岗位、角色名称唯一性校验参数："+ checkUniquenessDTO);
		return departmentService.checkUniqueness(checkUniquenessDTO);
	}


    @ApiOperation("查询部门列表  杨红梅")
    @PostMapping("listDepartment")
    public Result<PageResult<DepartmentVo>> listDepartment(@RequestBody DepartmentDto departmentDto){
        log.info("部门列表查询参数："+departmentDto);
        return departmentService.getDepartmentList(departmentDto);
    }

	@ApiOperation("新增部门  杨红梅")
	@PostMapping("/addDepartment")
	public Result addDepartment(@Validated @RequestBody DepartmentAddDTO addDTO){
		log.info("新增部门:"+addDTO);
		//获取操作人
		String username = this.getUserName();
		SystemDepartment department = new SystemDepartment();
		BeanUtils.copyProperties(addDTO, department);
		department.setOperationAccount(username);
		int result = departmentService.addDepartment(department);
		return Result.succeed(result);
	}

	@ApiOperation("修改部门  杨红梅")
	@PostMapping("/updateDepartment")
	public Result updateDepartment(@Validated @RequestBody DepartmentUpdateDTO updateDTO){
		log.info("修改部门:"+updateDTO);
		//获取操作人
		String username = getUserName();
		SystemDepartment department = new SystemDepartment();
		BeanUtils.copyProperties(updateDTO, department);
		department.setOperationAccount(username);
		departmentService.updateDepartment(department);
		return Result.succeed();
	}

	@ApiOperation("查询部门详情（入参为部门ID） 杨红梅")
	@PostMapping("getDepartmentDetail")
	public Result<DepartmentVo> getDepartmentDetail(@RequestBody BaseIdDTO baseIdDto){
    	String id = baseIdDto.getId();
		if(StringUtils.isBlank(id)){
			throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
		}
		DepartmentVo departmentVo = departmentService.getDepartmentAndParentById(id);
		return Result.succeed(departmentVo);
	}

	@ApiOperation("启用部门（入参为部门ID  杨红梅")
	@PostMapping("enableDepartment")
	public Result enableDepartment(@Validated @RequestBody BaseIdDTO baseIdDto){
		log.info("启用部门参数："+baseIdDto);
		String id = baseIdDto.getId();
		if(StringUtils.isBlank(id)){
			throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
		}
		//获取操作人
		String username = getUserName();
		log.info("操作人："+username);
		return departmentService.updateDepartmentStatus(id, CommonConstant.STATUS_ENABLE,username);
	}

	@ApiOperation("禁用部门（入参为部门ID） 杨红梅")
	@PostMapping("disableDepartment")
	public Result disableDepartment(@Validated @RequestBody BaseIdDTO baseIdDto){
		log.info("禁用部门参数："+baseIdDto);
		String id = baseIdDto.getId();
		if(StringUtils.isBlank(id)){
			throw new CustomException(CodeEnum.DEPARTMENTID_IS_EMPTY);
		}
		//获取操作人
		String username = getUserName();
		log.info("操作人："+username);
		return departmentService.updateDepartmentStatus(id, CommonConstant.STATUS_DISABLE,username);
	}

	@ApiOperation("获取树状结构部门信息  杨红梅")
	@PostMapping("getDepartmentTree")
	public Result<List<OrganazationVo>> getDepartmentTree(){
		List<OrganazationVo> departmentList = departmentService.getDepartmentTree();
		return Result.succeed(departmentList);
	}

	@ApiOperation("获取树状结构部门、岗位信息  杨红梅")
	@PostMapping("getDeptPositionTree")
	public Result<List<OrganazationVo>> getDeptPositionTree(){
		List<OrganazationVo> departmentList = departmentService.getDeptPositionTree();
		return Result.succeed(departmentList);
	}

	@ApiOperation("获取树状结构岗位信息  杨红梅")
	@PostMapping("getPositionTree")
	public Result<List<OrganazationVo>> getPositionTree(){
		List<OrganazationVo> positionList = positionService.getAllPositionTree();
		return Result.succeed(positionList);
	}

	@ApiOperation("根据部门id分页查询员工信息  杨红梅")
	@PostMapping("/getEmpLisByDeptId")
	public Result<PageResult<EmpDeptPositionVo>> getEmpLisByDeptId(
			@Validated @RequestBody OrganizationChartDto organizationChartDto) {
		JacksonUtil.dumnToPrettyJsonInfo("根据部门id查询所有员工信息 : ", organizationChartDto);
		Integer deptId = organizationChartDto.getDeptId();
		if (null == deptId) {
			return Result.failed("请传入部门id");
		}

		PageInfo<EmpDeptPositionVo> pageInfo = new PageInfo<>(departmentService.getEmpVoByDeptId(organizationChartDto));
		PageResult<EmpDeptPositionVo> pageResult = PageResult.getPageResult(pageInfo);

		ListHeaderUtil.setListHeaders(pageResult, EmpDeptPositionVo.class);
		return Result.succeed(pageResult);
    }

	@ApiOperation("根据岗位id分页查询员工信息  杨红梅")
	@PostMapping("/getEmpLisByPositionId")
	public Result<PageResult<EmpDeptPositionVo>> getEmpLisByPositionId(
			@Validated @RequestBody OrganizationChartDto organizationChartDto) {
		JacksonUtil.dumnToPrettyJsonInfo("根据岗位id查询所有员工信息 : ", organizationChartDto);
		Integer positionId = organizationChartDto.getPositionId();
		if (positionId == null) {
			return Result.failed("请传入岗位id");
		}

		PageInfo<EmpDeptPositionVo> pageInfo = new PageInfo<>(
				departmentService.getEmpLisByPositionId(organizationChartDto));
		PageResult<EmpDeptPositionVo> pageResult = PageResult.getPageResult(pageInfo);

		ListHeaderUtil.setListHeaders(pageResult, EmpDeptPositionVo.class);
		return Result.succeed(pageResult);
	}

	@ApiOperation("根据条件，分页查询员工信息  杨红梅")
	@PostMapping("/getEmpList")
	public Result<PageResult<EmpDeptPositionVo>> getEmpList(@Validated @RequestBody OrganizationChartDto organizationChartDto) {
		JacksonUtil.dumnToPrettyJsonInfo("根据条件，分页查询员工信息 : ", organizationChartDto);

		PageInfo<EmpDeptPositionVo> pageInfo = new PageInfo<>(departmentService.getEmpList(organizationChartDto));

		PageResult<EmpDeptPositionVo> pageResult = PageResult.getPageResult(pageInfo);
		ListHeaderUtil.setListHeaders(pageResult, EmpDeptPositionVo.class);
		return Result.succeed(pageResult);
	}


}

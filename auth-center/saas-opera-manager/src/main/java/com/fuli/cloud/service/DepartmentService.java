package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.CheckUniquenessDTO;
import com.fuli.cloud.dto.DepartmentDto;
import com.fuli.cloud.dto.OrganizationChartDto;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.vo.DepartmentVo;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.employee.EmpDeptPositionVo;

import java.util.List;
import java.util.Map;

/**
 * 部门服务接口
 */
public interface DepartmentService {

    /**
     * 查询部门列表
     * @param departmentDto 部门查询参数
     * @return
     */
    Result getDepartmentList(DepartmentDto departmentDto);

	/**
	 * 根据ID查询部门信息及上级部门名称
	 * @param id
	 * @return
	 */
	DepartmentVo getDepartmentAndParentById(String id);

    /**
     * 修改部门状态
     * @param departmentId 部门ID
     * @param status 状态
     * @param operationAccount 操作人
     * @return
     */
    Result updateDepartmentStatus(String departmentId, Byte status, String operationAccount);

    /**
     * 获取部门树
     * @return
     */
    List<OrganazationVo> getDepartmentTree();

	/**
	 * 获取部门岗位树
	 * @return
	 */
	List<OrganazationVo> getDeptPositionTree();

    /**
     * 新增部门
     * @author: zenglw
     * @Date: 2019/6/27 16:18
     * @param department
     * @return
     */
    int addDepartment(SystemDepartment department);

    /**
     * 修改部门
     * @author: zenglw
     * @Date: 2019/6/27 16:21
     * @return
     */
    int updateDepartment(SystemDepartment department);
	/**
	 * 
	 * <p>
	 * Description:返回字段包含岗位和部门名称
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年6月29日上午10:06:04
	 * @param organizationChartDto 部门id
	 * @return 返回字段包含岗位和部门名称
	 */
	List<EmpDeptPositionVo> getEmpVoByDeptId(OrganizationChartDto organizationChartDto);

	/**
	 *
	 * <p>
	 * Description: 返回字段包含岗位和部门名称
	 * </p>
	 *
	 * @author chenyi
	 * @date 2019年6月29日上午10:27:45
	 * @param organizationChartDto 岗位id
	 * @return
	 */
	List<EmpDeptPositionVo> getEmpLisByPositionId(OrganizationChartDto organizationChartDto);

	/**
	 *
	 * <p>
	 * Description: 返回字段包含岗位和部门名称
	 * </p>
	 *
	 * @author chenyi
	 * @date 2019年7月5日上午10:41:49
	 * @param organizationChartDto
	 * @return
	 */
	List<EmpDeptPositionVo> getEmpList(OrganizationChartDto organizationChartDto);


	/**
	 * <pre>
	 * Description: 根据名称查询一个启用的部门
	 * </pre>
	 *
	 * @param depName 部门名称
	 * @return 一个启用的部门
	 * @author chenyi
	 * @date 16:12 2019/8/1
	 **/
	SystemDepartment getOneByName(String depName);


	/**
	 * 部门、岗位、角色名称唯一性校验
	 * @author      WFZ
	 * @param 	    checkUniquenessDTO
	 * @return      Result
	 * @date        2019/8/31 17:47
	 */
	Result checkUniqueness(CheckUniquenessDTO checkUniquenessDTO);
}

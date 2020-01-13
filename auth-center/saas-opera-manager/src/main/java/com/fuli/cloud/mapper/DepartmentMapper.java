package com.fuli.cloud.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.DepartmentDto;
import com.fuli.cloud.dto.OrganizationChartDto;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.vo.DepartmentVo;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.employee.EmpDeptPositionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 部门Mapper接口
 * @author yhm
 * @date 2019/06/25
 */
@Mapper
@Repository
public interface DepartmentMapper extends BaseMapper<SystemDepartment> {

    /**
     * 查询部门列表
     * @return
     */
    List<DepartmentVo> getDepartmentList(DepartmentDto departmentDto);

	/**
	 * 根据ID查询部门信息及上级部门名称
	 * @param id
	 * @return
	 */
	DepartmentVo getDepartmentAndParentById(@Param("id") Serializable id);

    /**
     * 查询部门下未禁用的岗位数
     * @param departmentId 部门ID
     * @return
     */
    Integer getEnablePositionCount(@Param("departmentId") Integer departmentId);

    /**
     * 根据父级部门ID获取部门信息
     * @param departmentId 部门ID
     * @return
     */
    List<OrganazationVo> getDepartmentByParentId(@Param("parentId") Integer departmentId);


    /**
     * 通过部门编码获取部门信息
     * @author zenglw
     * @param code
     * @return
     */
    SystemDepartment getDepartmentByCode(@Param("code") String code);


	/**
	 *
	 * 根据部门id分页查询员工信息
	 * @author chenyi
	 * @date 2019年6月29日上午10:12:23
	 * @param organizationChartDto
	 * @return 返回字段包含岗位和部门名称
	 */
	List<EmpDeptPositionVo> getEmpVoByDeptId(OrganizationChartDto organizationChartDto);

	/**
	 *
	 * 根据岗位id分页查询员工信息-陈毅
	 * @author chenyi
	 * @date 2019年6月29日上午10:59:46
	 * @param organizationChartDto
	 * @return 返回字段包含岗位和部门名称
	 */
	List<EmpDeptPositionVo> getEmpLisByPositionId(OrganizationChartDto organizationChartDto);


	/**
	 * 获取所有有效的部门信息
	 * @return
	 */
	List<OrganazationVo> getAllDepartmentInfo();


	/**
	 *
	 * <p>
	 * Description:返回字段包含岗位和部门名称
	 * </p>
	 *
	 * @author chenyi
	 * @date 2019年7月5日上午10:49:53
	 * @param organizationChartDto
	 * @return
	 */
	List<EmpDeptPositionVo> getEmpList(OrganizationChartDto organizationChartDto);

	/**
	 * 根据部门名称查找部门
	 * @author zenglw
	 * @Date 2019/7/12 10:39
	 * @param name
	 * @param code
	 * @return com.basic.vo.DepartmentVo
	 */
	DepartmentVo getDepartmentByName(@Param("name") String name,@Param("code") String code);


	/**
	 * 更新子部门上级路径
	 * @param departmentId 部门ID
	 * @param oldParentId 原上级部门
	 * @param newParentId 现上级部门
	 * @return
	 */
	int updateDepartmentPathByParentId(@Param("departmentId") Integer departmentId, @Param("oldPath") String oldParentId, @Param("newPath") String newParentId);

	/**
	 * 去掉子部门上级路径的最后一个符号
	 * @param departmentId 部门ID
	 * @return
	 */
	int updatePathSeparateByParentId(@Param("departmentId") Integer departmentId);

	/**
	 * 查询上级部门是否是该部门的子级
	 * @param departmentId 部门ID
	 * @param parentId 上级部门ID
	 * @return
	 */
	int getChildDeptFlag(@Param("departmentId") Integer departmentId,@Param("parentId") Integer parentId);

	/**
	 * 禁用子级部门
	 * @param department  部门实体
	 * @return
	 */
	int disableChildDeptById(SystemDepartment department);
}
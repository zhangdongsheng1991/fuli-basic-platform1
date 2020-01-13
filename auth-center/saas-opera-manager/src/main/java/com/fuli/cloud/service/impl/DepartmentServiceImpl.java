package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.QWrapper;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.CheckUniquenessDTO;
import com.fuli.cloud.dto.DepartmentDto;
import com.fuli.cloud.dto.OrganizationChartDto;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.DepartmentMapper;
import com.fuli.cloud.mapper.PositionMapper;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.model.SystemRoleDO;
import com.fuli.cloud.service.DepartmentService;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.vo.DepartmentVo;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.PositionVo;
import com.fuli.cloud.vo.TableHeaderVO;
import com.fuli.cloud.vo.employee.EmpDeptPositionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 部门服务
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentDao;
    @Autowired
    private PositionService positionService;
    @Autowired
    private PositionMapper positionDao;
    @Autowired
    private SystemRoleMapper systemRoleMapper;

	/**
	 * 查询部门列表
	 * 
	 * @param departmentDto 部门查询参数
	 * @return
	 */
	@Override
    public Result getDepartmentList(DepartmentDto departmentDto){
        PageHelper.startPage(departmentDto.getPageNum(),departmentDto.getPageSize());
        List<DepartmentVo> list = departmentDao.getDepartmentList(departmentDto);
        PageInfo pageInfo = new PageInfo(list);
        PageResult<DepartmentVo> pageResult = PageResult.getPageResult(pageInfo);
        //组装表头
        List<TableHeaderVO> tableHeaders = new ArrayList<>();
        tableHeaders.add(new TableHeaderVO("code","部门编码"));
        tableHeaders.add(new TableHeaderVO("name","部门名称"));
        tableHeaders.add(new TableHeaderVO("employeeCount","人数"));
        tableHeaders.add(new TableHeaderVO("createTime","创建时间"));
        tableHeaders.add(new TableHeaderVO("operationAccount","操作人"));
        tableHeaders.add(new TableHeaderVO("updateTime","更新时间"));
        tableHeaders.add(new TableHeaderVO("status","状态"));
        pageResult.setTableHeaders(tableHeaders);

        return Result.succeed(pageResult);
    }

    /**
     * 新增部门
     * @author: zenglw
     * @Date: 2019/6/27 16:18
     * @param department
     * @return
     */
    @Transactional
    @Override
    public int addDepartment(SystemDepartment department) {
        int result = 0;
        // 判断部门名称是否存在
        DepartmentVo departmentVo = departmentDao.getDepartmentByName(department.getName(),null);
        if (departmentVo != null){
            throw new ServiceException(CodeEnum.DEPARTMENT_NAME_ALREADY_EXIST);
        }
        this.checkDepartment(department);
        if (StringUtils.isNotBlank(department.getCode())){
            // 校验部门编码是否存在
            SystemDepartment deptByCode = departmentDao.getDepartmentByCode(department.getCode());
            if (deptByCode != null){
                throw new ServiceException(CodeEnum.DEPARTMENT_CODE_ALREADY_EXIST);
            }
        }
        try {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            // 添加操作
            departmentDao.insert(department);
            result =  department.getId();
        } catch (Exception e) {
            log.error("新增部门信息失败{}",e);
            throw new ServiceException(CodeEnum.ERROR.getCode(),"新增部门信息失败，请稍后再试");
        }
        return result;
    }

    /**
     * 修改部门
     * @author: zenglw
     * @Date: 2019/6/27 16:21
     * @param department
     * @return
     */
    @Transactional
    @Override
    public int updateDepartment(SystemDepartment department) {
        SystemDepartment oldDepartment = departmentDao.selectById(department.getId());
        if(oldDepartment == null){
            throw new ServiceException(CodeEnum.DEPARTMENT_NOTFOUND);
        }

        // 判断部门名称是否存在
        DepartmentVo departmentVo = departmentDao.getDepartmentByName(department.getName(),null);
        if (departmentVo != null && !department.getId().equals(departmentVo.getId())){
            throw new ServiceException(CodeEnum.DEPARTMENT_NAME_ALREADY_EXIST);
        }

        this.checkDepartment(department);
        if (StringUtils.isNotBlank(department.getCode())){
            // 校验部门编码 是否存在
            SystemDepartment deptByCode = departmentDao.getDepartmentByCode(department.getCode());
            if (deptByCode != null && !deptByCode.getId().equals(department.getId())){
                throw new CustomException(CodeEnum.DEPARTMENT_CODE_ALREADY_EXIST);
            }
        }
        // 上级部门不能为自身
        if (department.getId() != null && department.getParentId() != null && department.getId().equals(department.getParentId())){
            throw new CustomException(CodeEnum.DEPARTMENT_PARENT_NOT_IS_SELF);
        }

        //校验上级部门不能为它的下级部门
        int count = departmentDao.getChildDeptFlag(department.getId(),department.getParentId());
        if(count > 0){
            throw new CustomException(CodeEnum.PARENT_DEPARTMENT_ISCHILD);
        }
        try {
            if (department.getParentId() == null ){
                department.setParentId(0);
                department.setPath("");
            }
            department.setUpdateTime(new Date());
            departmentDao.updateById(department);

            //如果修改了上级部门，则需变更所有子部门的上级路径
            if(!department.getParentId().equals(oldDepartment.getParentId())){
                //原有上级路径
                String oldPath = StringUtils.isBlank(oldDepartment.getPath()) ? oldDepartment.getId()+"" : oldDepartment.getPath()+","+oldDepartment.getId();
                //现有上级路径
                String newPath = StringUtils.isBlank(department.getPath()) ? department.getId()+"" : department.getPath()+","+department.getId();
                //变更上级路径
                departmentDao.updateDepartmentPathByParentId(department.getId(),oldPath,newPath);
                //去掉上级路径多余的分隔符
                departmentDao.updatePathSeparateByParentId(department.getId());

                /***************变更该部门下有上级部门的上级岗位的岗位******************/
                //查询有上级部门的上级岗位的岗位
                Integer parentId = oldDepartment.getParentId();
                List<PositionVo> list = positionDao.getParentDeptPositionByParentId(department.getId(),parentId);
                if(list != null && list.size() > 0){
                    for(PositionVo positionVo : list){
                        String path = positionVo.getPath()+","+positionVo.getId();
                        //将上级岗位置空
                        SystemPosition position = new SystemPosition();
                        position.setId(positionVo.getId());
                        position.setParentId(0);
                        position.setPath("");
                        positionDao.updateById(position);
                        //变更子岗位的上级路径
                        positionDao.updatePositionPathByParentId(positionVo.getId(),path,String.valueOf(positionVo.getId()));
                        //去掉上级路径多余的分隔符
                        positionDao.updatePathSeparateByParentId(positionVo.getId());
                    }

                }
            }

        } catch (Exception e) {
            log.error("修改部门信息失败{}",e);
            throw new ServiceException(CodeEnum.ERROR.getCode(),"修改部门信息失败，请稍后再试");
        }
        return 1;
    }

    private void checkDepartment(SystemDepartment department){
        // 校验父级是否存在
        if(department.getParentId() != null && department.getParentId() != 0){
            SystemDepartment parentDept = departmentDao.selectById(department.getParentId());
            if (parentDept == null){
                throw new ServiceException(CodeEnum.DEPARTMENT_PARENT_NOT_EXIST);
            }
            // 设置部门path
            String path = StringUtils.isBlank(parentDept.getPath()) ? String.valueOf(department.getParentId()) : parentDept.getPath()+","+department.getParentId();
            department.setPath(path);
        }
    }

    /**
     * 根据ID查询部门信息及上级部门名称
     * @param id
     * @return
     */
    @Override
    public DepartmentVo getDepartmentAndParentById(String id){
        DepartmentVo departmentVo = departmentDao.getDepartmentAndParentById(id);
        if(departmentVo == null){
            throw new CustomException(CodeEnum.DEPARTMENT_NOTFOUND);
        }
        return departmentVo;
    }

    /**
     * 修改部门状态
     * @param departmentIdStr 部门ID
     * @param status 状态
     * @return
     */
    @Transactional
    @Override
    public Result updateDepartmentStatus(String departmentIdStr, Byte status,String operationAccount){
        //查询部门信息
        SystemDepartment department = departmentDao.selectById(departmentIdStr);
        //部门不存在
        if(department == null){
            throw new CustomException(CodeEnum.DEPARTMENT_NOTFOUND);
        }
        //状态错误
        if(department.getStatus().byteValue() == status){
            throw new CustomException(CodeEnum.STATUS_ERROR);
        }
        Integer departmentId = Integer.parseInt(departmentIdStr);
        //禁用操作
        if(status == CommonConstant.STATUS_DISABLE){
            //检查该部门及下级部门下是否有启用的岗位
            Integer enableCount = departmentDao.getEnablePositionCount(departmentId);
            if(enableCount != null && enableCount.intValue() > 0){
                throw new CustomException(CodeEnum.DEPARTMENT_DISABLE_ERROR);
            }
        }

        //启用操作
        if(status == CommonConstant.STATUS_ENABLE){
            //检查该父级部门是否有被禁用
            Integer parentId = department.getParentId();
            if(parentId != null && parentId.intValue() != 0){
                SystemDepartment parentDept = departmentDao.selectById(parentId);
                if(parentDept != null && parentDept.getStatus() == CommonConstant.STATUS_DISABLE){
                    throw new CustomException(CodeEnum.ENABLE_PARENT_DEPT);
                }
            }
        }

        department.setStatus(status);
        department.setOperationAccount(operationAccount);
        department.setUpdateTime(new Date());
        //更新部门状态信息
        int result = departmentDao.updateById(department);
        if(result > 0){
            if(status == CommonConstant.STATUS_DISABLE){
                //如果是禁用操作，更新该部门及该部门的所有子部门状态为禁用
                departmentDao.disableChildDeptById(department);
            }
            return Result.succeed();
        }
        return Result.failed();
    }

    //所有有效的部门列表
    private List<OrganazationVo> allDeptList = new ArrayList<OrganazationVo>();  //全局变量

    /**
     * 获取部门树
     * @return
     */
    @Override
    public List<OrganazationVo> getDepartmentTree(){
        //获取所有有效的部门信息
        allDeptList = departmentDao.getAllDepartmentInfo();

        //获取一级部门
        List<OrganazationVo> list = departmentDao.getDepartmentByParentId(0);
        if(list != null && list.size() > 0){
            //遍历部门列表
            for(OrganazationVo department : list){
                List<OrganazationVo> childList = getChildDepartment(department);
                department.setChildren(childList);
            }
        }
        return list;
    }

    /**
     * 递归获取子级部门信息
     * @param departmentVo 部门实体
     * @return
     */
    List<OrganazationVo> getChildDepartment(OrganazationVo departmentVo){
        List<OrganazationVo> tempList = new ArrayList<OrganazationVo>();
            //查询该子级部门信息
            tempList = getChildInfoById(departmentVo.getId());
            if(tempList != null && tempList.size() > 0){
                //遍历部门列表
                for(OrganazationVo department : tempList){
                    List<OrganazationVo> childList = this.getChildDepartment(department);
                    department.setChildren(childList);
                }
            }
//        }
        return tempList;
    }

    /**
     * 根据ID获取子级部门信息
     * @param id 部门ID
     * @return
     */
    List<OrganazationVo> getChildInfoById(Integer id){
        List<OrganazationVo> list = new ArrayList<OrganazationVo>();
        //遍历所有部门列表找到子级
        for(OrganazationVo deptVo : allDeptList){
            if(deptVo.getParentId().equals(id)){
                list.add(deptVo);
            }
        }
        return list;
    }

    /**
     * 获取部门、岗位树
     * @return
     */
    @Override
    public List<OrganazationVo> getDeptPositionTree(){
        //获取所有有效的部门信息
        allDeptList = departmentDao.getAllDepartmentInfo();

        //获取一级部门
        List<OrganazationVo> list = departmentDao.getDepartmentByParentId(0);
        if(list != null && list.size() > 0){
            //遍历部门列表
            for(OrganazationVo department : list){
                List<OrganazationVo> childList = getChildDeptPosition(department);
                //获取该部门的岗位
                List<OrganazationVo> positionList = positionService.getDeptPositionTree(String.valueOf(department.getId()),0,0,null);
                childList.addAll(positionList);
                department.setChildren(childList);
            }
        }
        return list;
    }

    /**
     * 递归获取子级部门、岗位信息
     * @param departmentVo 部门实体
     * @return
     */
    List<OrganazationVo> getChildDeptPosition(OrganazationVo departmentVo){
        List<OrganazationVo> tempList = new ArrayList<OrganazationVo>();
//            tempList = departmentDao.getDepartmentByParentId(departmentVo.getCompanyId(),departmentVo.getId());

            //查询该子级部门信息
            tempList = this.getChildInfoById(departmentVo.getId());
            if(tempList != null && tempList.size() > 0){
                //遍历部门列表
                for(OrganazationVo department : tempList){
                    List<OrganazationVo> childList = this.getChildDeptPosition(department);
                    //获取该部门的岗位
                    List<OrganazationVo> positionList = positionService.getDeptPositionTree(String.valueOf(department.getId()),0,0,null);

                    childList.addAll(positionList);
                    department.setChildren(childList);
                }
            }
        return tempList;
    }

	@Override
	public List<EmpDeptPositionVo> getEmpVoByDeptId(OrganizationChartDto organizationChartDto) {
		Integer deptId = organizationChartDto.getDeptId();
		if (deptId == null) {
			throw new ServiceException(CodeEnum.DEPT_ID_IS_NULL);
		}
		PageHelper.startPage(organizationChartDto.getPageNo(), organizationChartDto.getPageSize());
		return departmentDao.getEmpVoByDeptId(organizationChartDto);
	}

	@Override
	public List<EmpDeptPositionVo> getEmpLisByPositionId(OrganizationChartDto organizationChartDto) {
		Integer positionId = organizationChartDto.getPositionId();
		if (positionId == null) {
			throw new ServiceException(CodeEnum.POSITION_ID_IS_NULL);
		}
		PageHelper.startPage(organizationChartDto.getPageNo(), organizationChartDto.getPageSize());

		return departmentDao.getEmpLisByPositionId(organizationChartDto);

	}

    @Override
    public List<EmpDeptPositionVo> getEmpList(OrganizationChartDto organizationChartDto) {

        PageHelper.startPage(organizationChartDto.getPageNo(), organizationChartDto.getPageSize());

        return departmentDao.getEmpList(organizationChartDto);
    }

    /**
     *  两层循环包装完整树
     * @author zenglw
     * @Date 2019/7/1 15:47
     * @param listDept
     * @return java.util.List<com.basic.vo.DepartmentVo>
     */
    private List<DepartmentVo> parckingDepartmentTree(List<DepartmentVo> listDept){
        List<DepartmentVo> trees = new ArrayList<>();
        for (DepartmentVo dept: listDept){
            if (dept.getParentId() != null && dept.getParentId().equals(0)){
                trees.add(dept);
            }
            for (DepartmentVo dt : listDept) {
                if (dt.getParentId() !=null && dept.getId() != null && dt.getParentId().equals(dept.getId())) {
                    if (dept.getChildren() == null) {
                        dept.setChildren(new ArrayList<>());
                    }
                    dept.getChildren().add(dt);
                }
            }
        }
        return trees;
    }

    @Override
    public SystemDepartment getOneByName(String depName) {

        QWrapper<SystemDepartment> qWrapper = new QWrapper<>();
        qWrapper.select(SystemDepartment.Fields.id, SystemDepartment.Fields.name)
                .eq(SystemDepartment.Fields.status, 1)
                .eq(SystemDepartment.Fields.name, depName).last("limit 1");

        return this.departmentDao.selectOne(qWrapper);
    }

    /**
     * 部门、岗位、角色名称唯一性校验
     *
     * @param dto
     * @return Result
     * @author WFZ
     * @date 2019/8/31 17:47
     */
    @Override
    public Result checkUniqueness(CheckUniquenessDTO dto) {
        if (StringUtils.isNotBlank(dto.getName()) || StringUtils.isNotBlank(dto.getCode())){
            if ("1".equals(dto.getType())){
                DepartmentVo departmentVo = departmentDao.getDepartmentByName(dto.getName(),dto.getCode());
                if (departmentVo != null){
                    if( StringUtils.isBlank(dto.getId()) || ( StringUtils.isNotBlank(dto.getId()) && ! dto.getId().equals(departmentVo.getId().toString()) ) ){
                        if (StringUtils.isNotBlank(dto.getName())){
                            return Result.failed(CodeEnum.DEPARTMENT_NAME_ALREADY_EXIST);
                        }else {
                            return Result.failed(CodeEnum.DEPARTMENT_CODE_ALREADY_EXIST);
                        }
                    }
                }
            }else if ("2".equals(dto.getType())){
                SystemPosition p = new SystemPosition();
                p.setName(dto.getName());
                p.setCode(dto.getCode());
                SystemPosition rPosition = positionDao.getCompanyPosition(p);
                if(rPosition != null ){
                    if( StringUtils.isBlank(dto.getId()) || ( StringUtils.isNotBlank(dto.getId()) && ! dto.getId().equals(rPosition.getId().toString()) ) ){
                        if (StringUtils.isNotBlank(dto.getName())){
                            return Result.failed(CodeEnum.POSITION_NAME_EXISTS);
                        }else {
                            return Result.failed(CodeEnum.POSITION_CODE_EXISTS);
                        }
                    }
                }
            }else if ("3".equals(dto.getType())){
                QueryWrapper<SystemRoleDO> queryWrapper = new QueryWrapper<>();
                queryWrapper.select("id");
                queryWrapper.eq("name", dto.getName());
                queryWrapper.ne(dto.getId() != null, "id", dto.getId());
                queryWrapper.last("limit 1");
                SystemRoleDO role = systemRoleMapper.selectOne(queryWrapper);
                if(role != null){
                    return Result.failed(CodeEnum.ROLE_NAME_EXISTS);
                }
            }else {
                return Result.failed("非法类型");
            }
            return Result.succeed();
        }
        return Result.failed();
    }
}

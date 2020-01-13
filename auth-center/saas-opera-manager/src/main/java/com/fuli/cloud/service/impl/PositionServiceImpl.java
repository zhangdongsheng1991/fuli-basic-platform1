package com.fuli.cloud.service.impl;

import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.QWrapper;
import com.fuli.cloud.constant.CommonConstant;
import com.fuli.cloud.dto.PositionDto;
import com.fuli.cloud.handler.CustomException;
import com.fuli.cloud.mapper.DepartmentMapper;
import com.fuli.cloud.mapper.PositionMapper;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.PositionVo;
import com.fuli.cloud.vo.TableHeaderVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 岗位服务
 * @author yhm
 * @date 2019/06/25
 */
@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    PositionMapper positionDao;
    @Autowired
    DepartmentMapper departmentDao;
    /**
     * 查询岗位列表
     * @param positionDto 岗位查询参数
     * @return
     */
    @Override
    public Result getPositionList(PositionDto positionDto){
        PageHelper.startPage(positionDto.getPageNum(),positionDto.getPageSize());
        List<PositionVo> list = positionDao.getPositionList(positionDto);
        PageInfo pageInfo = new PageInfo(list);
        PageResult<PositionVo> pageResult = PageResult.getPageResult(pageInfo);

        //组装表头
        List<TableHeaderVO> tableHeaders = new ArrayList<>();
        tableHeaders.add(new TableHeaderVO("code","岗位编码"));
        tableHeaders.add(new TableHeaderVO("name","岗位名称"));
        tableHeaders.add(new TableHeaderVO("employeeCount","人数"));
        tableHeaders.add(new TableHeaderVO("createTime","创建时间"));
        tableHeaders.add(new TableHeaderVO("operationAccount","操作人"));
        tableHeaders.add(new TableHeaderVO("updateTime","更新时间"));
        tableHeaders.add(new TableHeaderVO("status","状态"));
        pageResult.setTableHeaders(tableHeaders);

        return Result.succeed(pageResult);
    }

    /**
     * 根据ID获取岗位信息及父级名称
     * @param positionId 岗位ID
     * @return
     */
    @Override
    public PositionVo getPositionAndParentById(String positionId){
        PositionVo positionVo = positionDao.getPositionAndParentById(positionId);
        if(positionVo == null){
            throw new CustomException(CodeEnum.POSITION_NOTFOUND);
        }
        return positionVo;
    }

    /**
     * 修改岗位状态
     * @param positionId 岗位ID
     * @param status 状态
     * @param operationAccount 操作人
     * @return
     */
    @Transactional
    @Override
    public Result updatePositionStatus(String positionId, Byte status,String operationAccount){
        //查询岗位信息
        SystemPosition position = positionDao.selectById(positionId);
        //岗位不存在
        if(position == null){
            throw new CustomException(CodeEnum.POSITION_NOTFOUND);
        }
        //状态错误
        if(position.getStatus().byteValue() == status){
            throw new CustomException(CodeEnum.STATUS_ERROR);
        }
        //禁用操作
        if(status == CommonConstant.STATUS_DISABLE){
            //检查该岗位及下级岗位下是否有启用的员工
            Integer enableCount = positionDao.getEnableEmployeeCount(positionId);
            if(enableCount != null && enableCount.intValue() > 0){
                throw new CustomException(CodeEnum.POSITION_DISABLE_ERROR);
            }
        }

        //启用操作
        if(status == CommonConstant.STATUS_ENABLE){
            /** 判断该岗位所属部门是否启用*/
            SystemDepartment department = departmentDao.selectById(position.getDepartmentId());
            if (department != null && department.getStatus().intValue() == CommonConstant.STATUS_DISABLE){
                return Result.failed("请先启用该岗位所属部门");
            }
            //检查该父级岗位是否有被禁用
            Integer parentId = position.getParentId();
            if(parentId != null && parentId.intValue() != 0){
                SystemPosition parentPosition = positionDao.selectById(parentId);
                if(parentPosition != null && parentPosition.getStatus() == CommonConstant.STATUS_DISABLE){
                    throw new CustomException(CodeEnum.ENABLE_PARENT_POSITION);
                }
            }
        }

        position.setStatus(status);
        position.setOperationAccount(operationAccount);
        position.setUpdateTime(new Date());
        //更新
        int result = positionDao.updateById(position);
        if(result > 0){
            //更新该岗位下所有子级岗位状态为禁用
            if(status == CommonConstant.STATUS_DISABLE){
                positionDao.disableChildPositonById(position);
            }
            return Result.succeed();
        }
        return Result.failed();
    }

    /**
     * 新增岗位
     * @param position 岗位实体类
     * @return
     */
    @Transactional
    @Override
    public Result addPosition(SystemPosition position){
        //校验新增岗位信息
        String parentPath = validatePosition(position,Boolean.TRUE);
        position.setPath(parentPath);

        position.setCreateTime(new Date());
        position.setUpdateTime(new Date());
        //启用
        position.setStatus(CommonConstant.STATUS_ENABLE);
        //修改
        int result =  positionDao.insert(position);
        if(result > 0){
            return Result.succeed();
        }
        return Result.failed();
    }

    /**
     * 修改岗位
     * @param position 岗位实体类
     * @return
     */
    @Transactional
    @Override
    public Result updatePosition(SystemPosition position){
        //查询岗位信息
        SystemPosition oldPosition = positionDao.selectById(position.getId());
        //岗位不存在
        if(oldPosition == null){
            throw new CustomException(CodeEnum.POSITION_NOTFOUND);
        }

        //校验已禁用的岗位不能修改
        if(oldPosition.getStatus() == CommonConstant.STATUS_DISABLE){
            throw new CustomException(CodeEnum.POSITION_NOTEDIT_DISABLED);
        }

        //校验不能修改部门
        if(oldPosition.getDepartmentId().intValue() != position.getDepartmentId()){
            throw new CustomException(CodeEnum.DEPARTMENT_CANNOT_EDIT);
        }

        //校验修改岗位信息
        String parentPath = validatePosition(position,Boolean.FALSE);
        position.setPath(parentPath);

        // 校验上级岗位不能为本岗位
        if (position.getParentId() != null && position.getParentId().equals(position.getId())){
            throw new CustomException(CodeEnum.PARENT_POSITION_ISSELF);
        }

        //校验上级岗位不能为它的下级岗位
        int count = positionDao.getChildPositionFlag(position.getId(),position.getParentId());
        if(count > 0){
            throw new CustomException(CodeEnum.PARENT_POSITION_ISCHILD);
        }

        if(position.getParentId() == null){
            position.setParentId(0);
        }

        position.setUpdateTime(new Date());

        //修改
        int result =  positionDao.updateById(position);

        //如果修改了上级岗位，则需变更所有子岗位的上级路径
        if(!position.getParentId().equals(oldPosition.getParentId())){
            //原有上级路径
            String oldPath = StringUtils.isBlank(oldPosition.getPath()) ? position.getId()+"" : oldPosition.getPath()+","+position.getId();
            //现有上级路径
            String newPath = StringUtils.isBlank(parentPath) ? position.getId()+"" : parentPath+","+position.getId();
            //变更上级路径
            positionDao.updatePositionPathByParentId(position.getId(),oldPath,newPath);
            //去掉上级路径多余的分隔符
            positionDao.updatePathSeparateByParentId(position.getId());
        }

        if(result > 0){
            return Result.succeed();
        }
        return Result.failed();
    }


    /**
     * 校验需要新增/修改的岗位信息
     * @param position 岗位信息
     * @param addFlag 是否为新增
     * @return 上级路径
     */
    private String validatePosition(SystemPosition position, boolean addFlag){
        //上级路径
        String parentPath = "";
        //查询部门信息
        SystemDepartment department = departmentDao.selectById(position.getDepartmentId());
        //部门不存在
        if(department == null){
            throw new CustomException(CodeEnum.DEPARTMENT_NOTFOUND);
        }
        //校验状态
        if(department.getStatus() != CommonConstant.STATUS_ENABLE){
            throw new CustomException(CodeEnum.PARENT_DEPARTMENT_STATUS_ERROR);
        }

        //如果部门不是顶级部门，则上级岗位必填
        if(department.getParentId().intValue() != 0 && (position.getParentId() == null || position.getParentId().intValue() == 0)){
            throw new CustomException(CodeEnum.PARENT_POSITION_ISEMPTY);
        }

        //校验该企业下是否存在相同的名称
        if(StringUtils.isNotBlank(position.getName())){
            SystemPosition p = new SystemPosition();
            p.setName(position.getName());
            SystemPosition rPosition = positionDao.getCompanyPosition(p);
            //该名称已存在
            if(rPosition != null){
                //新增、修改（不是同一个岗位），抛出异常
                if(addFlag || (!addFlag && rPosition.getId().intValue() != position.getId().intValue())){
                    throw new CustomException(CodeEnum.POSITION_NAME_EXISTS);
                }
            }
        }

        //校验该企业下是否存在相同的编码
        if(StringUtils.isNotBlank(position.getCode())){
            SystemPosition cPosition = new SystemPosition();
//            cPosition.setCompanyId(position.getCompanyId());
            cPosition.setCode(position.getCode());
            SystemPosition rcPosition = positionDao.getCompanyPosition(cPosition);
            //该编码已存在
            if(rcPosition != null){
                //新增、修改（不是同一个岗位），抛出异常
                if(addFlag || (!addFlag && rcPosition.getId().intValue() != position.getId().intValue())){
                    throw new CustomException(CodeEnum.POSITION_CODE_EXISTS);
                }
            }
        }

        //获取上级岗位
        Integer parentId = position.getParentId();
        if(parentId != null && parentId.intValue() != 0){
            SystemPosition pPosition = positionDao.selectById(parentId);
            //上级岗位不存在
            if(pPosition == null){
                throw new CustomException(CodeEnum.PARENT_POSITION_NOTFOUND);
            }
            //状态错误
            if(pPosition.getStatus() != CommonConstant.STATUS_ENABLE){
                throw new CustomException(CodeEnum.PARENT_POSITION_STATUS_ERROR);
            }

            //校验上级岗位所在部门与传入的部门是否一致
//            if(pPosition.getDepartmentId().intValue() != position.getDepartmentId().intValue()){
//                throw new CustomException(CodeEnum.PARENT_POSITION_COMPANY_ERROR);
//            }
            //设置岗位的上级路径
            String separate = ",";
            if(StringUtils.isNotBlank(pPosition.getPath())){
                parentPath = pPosition.getPath() + separate +parentId;
            }
            else{
                parentPath = parentId+"";
            }

        }else{
            parentPath = "";
        }
        return parentPath;
    }


    //所有有效的岗位列表
    //private List<PositionVo> allPositionList = new ArrayList<PositionVo>();  //全局变量
    /**
     * 获取部门ID岗位树
     * @param departmentId 部门ID
     * @return
     */
    @Override
    public List<OrganazationVo> getPositionTree(Integer departmentId,Integer positionId){
        //获取所有有效岗位列表
        PositionDto positionDto = new PositionDto();
        positionDto.setDepartmentId(departmentId);
        positionDto.setId(positionId);
        List<OrganazationVo> allPositionList = positionDao.getAllPositionInfo(positionDto);

        //获取一级岗位
        List<OrganazationVo> list = positionDao.getPositionByParentId(departmentId,0,positionId);
        if(list != null && list.size() > 0){
            //遍历岗位列表
            for(OrganazationVo position : list){
                List<OrganazationVo> childList = getChildPosition(position,allPositionList);
                position.setChildren(childList);
            }
        }
        return list;
    }

    /**
     * 递归获取子级岗位信息
     * @param positionVo 岗位实体
     * @return
     */
    private List<OrganazationVo> getChildPosition(OrganazationVo positionVo,List<OrganazationVo> allPositionList){
        List<OrganazationVo> tempList = new ArrayList<OrganazationVo>();
            //查询该子级岗位信息
           tempList = getChildById(positionVo.getId(),allPositionList);
            if(tempList != null && tempList.size() > 0){
                //遍历岗位列表
                for(OrganazationVo position : tempList){
                    List<OrganazationVo> childList = this.getChildPosition(position,allPositionList);
                    position.setChildren(childList);
                }
            }
        return tempList;
    }

    /**
     * 根据ID获取子级岗位信息
     * @param id 岗位ID
     * @return
     */
    List<OrganazationVo> getChildById(Integer id,List<OrganazationVo> allPositionList){
        List<OrganazationVo> list = new ArrayList<OrganazationVo>();
        for(OrganazationVo positionVo : allPositionList){
            if(positionVo.getParentId().equals(id)){
               list.add(positionVo);
            }
        }
        return list;
    }

    /**
     * 获取上级岗位树
     * @param departmentId 部门ID
     * @param positionId 岗位id
     * @return
     */
    @Override
    public List<OrganazationVo> getParentPositionTree(Integer departmentId,Integer positionId){
        List<OrganazationVo> resultList = new ArrayList<OrganazationVo>();
        //查询该部门是否是顶级部门
        SystemDepartment department = departmentDao.selectById(departmentId);
        //部门不存在
        if(department  == null){
            throw new CustomException(CodeEnum.DEPARTMENT_NOTFOUND);
        }

        //是顶级部门
        if(department.getParentId().intValue() == 0){
            //获取本部门下的岗位树
            resultList = getPositionTree(departmentId,positionId);
        }else{//不是顶级部门
            /****************获取上级部门的岗位树************************/
            List parentList = getDeptPositionTree(String.valueOf(department.getParentId()),departmentId,1,positionId);
            if (parentList != null && parentList.size() > 0) {
                resultList.addAll(parentList);
            }

            /***************获取本级部门的岗位树**************************/
            //获取该部门下所有有效岗位列表
            PositionDto positionDto = new PositionDto();
            positionDto.setDepartmentId(departmentId);
            positionDto.setId(positionId);
            List<OrganazationVo> allPositionList = positionDao.getAllPositionInfo(positionDto);
            List<OrganazationVo> deptList = getSelfRootPosition(departmentId,allPositionList,positionId);
            if(deptList != null && deptList.size() > 0){
                resultList.addAll(deptList);
            }

        }
        return resultList;
    }


    /**
     * 查询部门岗位树
     * @param departmentIdStr 部门ID
     * @param childDeptId 指定下一级部门
     * @param showFlag 查询下级部门岗位标识 0：不查询 1：查询下一级部门岗位 2：查询所有下级部门岗位
     * @param positionId 需要过滤的岗位id
     * @return
     */
    @Override
    public List<OrganazationVo> getDeptPositionTree(String departmentIdStr,Integer childDeptId,int showFlag,Integer positionId){
        //查询部门是否存在
        SystemDepartment dept = departmentDao.selectById(departmentIdStr);
        if(dept == null){
            throw new ServiceException(CodeEnum.DEPARTMENT_NOTFOUND);
        }
        Integer departmentId = Integer.parseInt(departmentIdStr);

        List<OrganazationVo> resultList = new ArrayList<OrganazationVo>();
        List<OrganazationVo> allPositionList = new ArrayList<OrganazationVo>();
        if(showFlag == 0){
            //获取该部门下所有有效岗位列表
            PositionDto positionDto = new PositionDto();
            positionDto.setDepartmentId(departmentId);
            positionDto.setId(positionId);
            allPositionList = positionDao.getAllPositionInfo(positionDto);
        }else if(showFlag == 1){
            //获取指定2级（本级、下级）部门的所有有效岗位列表
            allPositionList = positionDao.getPositionAndNextChild(departmentId,childDeptId,positionId);
        }else if(showFlag == 2){
            //获取本部门及下级所有部门的有效岗位列表
            allPositionList = positionDao.getPositionAndChilds(departmentId,positionId);
        }

        /*********************上级岗位是上级部门的树********************/
        //查询上级岗位是上级部门的岗位
        List<OrganazationVo> parentList = positionDao.getParentDeptPosition(departmentId,positionId);
        if(parentList != null && parentList.size() > 0){
            //遍历岗位列表
            for(OrganazationVo position : parentList){
                List<OrganazationVo> childList = getChildPosition(position,allPositionList);
                position.setChildren(childList);
            }
            resultList.addAll(parentList);
        }

        /****************岗位是本部门顶级岗位的树*****************/
        List<OrganazationVo> list = getSelfRootPosition(departmentId,allPositionList,positionId);
        if(list != null && list.size() > 0){
            resultList.addAll(list);
        }
        return resultList;
    }

    /**
     * 获取本部门根级岗位树
     * @param departmentId  部门id
     * @param deptPositionList 本部门岗位列表
     * @return
     */
    List<OrganazationVo> getSelfRootPosition(Integer departmentId,List<OrganazationVo> deptPositionList,Integer positionId){
        //获取一级岗位
        List<OrganazationVo> list = positionDao.getPositionByParentId(departmentId,0,positionId);
        if(list != null && list.size() > 0){
            //遍历岗位列表
            for(OrganazationVo position : list){
                List<OrganazationVo> childList = getChildPosition(position,deptPositionList);
                position.setChildren(childList);
            }
        }
        return list;
    }

    /**
     * 获取整个岗位树
     * @return
     */
    @Override
    public List<OrganazationVo> getAllPositionTree(){
        //获取所有有效岗位列表
        List<OrganazationVo> allPositionList = positionDao.getPositions();
        //获取一级岗位
        List<OrganazationVo> list = positionDao.getRootPosition();
        if(list != null && list.size() > 0){
            //遍历岗位列表
            for(OrganazationVo position : list){
                List<OrganazationVo> childList = getChildPosition(position,allPositionList);
                position.setChildren(childList);
            }
        }
        return list;
    }


    @Override
    public SystemPosition getOneByName(String position) {
        QWrapper<SystemPosition> qWrapper = new QWrapper<>();
        qWrapper.select(SystemPosition.Fields.id, SystemPosition.Fields.name, "status", "department_id", "parent_id")
                .eq(SystemPosition.Fields.status, 1)
                .eq(SystemPosition.Fields.name, position).last("limit 1");

        return this.positionDao.selectOne(qWrapper);
    }

    @Override
    public boolean checkPositionIsSlaveOfDept(SystemPosition position, Integer deptId, int count) {

        if (count > 500) {
            return false;
        }

        if (position.getDepartmentId() == null) {
            return false;
        }

        if (position.getDepartmentId().intValue() == deptId.intValue()) {
            return true;
        }
        // 查询父岗位
        Integer parentId = position.getParentId();
        if (parentId == 0) {
            return false;
        }
        SystemPosition parentPosition = this.positionDao.selectById(parentId);
        if (parentPosition == null || parentPosition.getStatus() != 1) {
            return false;
        }

        return checkPositionIsSlaveOfDept(parentPosition, deptId, count);
    }
}

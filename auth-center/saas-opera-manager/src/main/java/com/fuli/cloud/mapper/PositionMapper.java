package com.fuli.cloud.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fuli.cloud.dto.PositionDto;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.PositionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

/**
 * 岗位Mapper接口
 * @author yhm
 * @date 2019/06/25
 */

@Mapper
@Repository
public interface PositionMapper extends BaseMapper<SystemPosition> {

    /**
     * 查询岗位列表
     * @param positionDto 岗位入参类
     * @return
     */
    List<PositionVo> getPositionList(PositionDto positionDto);

    /**
     * 获取所有有效岗位信息
     * @param positionDto 岗位入参
     * @return
     */
    List<OrganazationVo> getAllPositionInfo(PositionDto positionDto);

    /**
     * 根据ID获取岗位信息
     * @param positionId 岗位ID
     * @return
     */
    PositionVo getPositionById(@Param("id") Integer positionId);

    /**
     * 根据ID获取岗位信息及父级名称
     * @param positionId 岗位ID
     * @return
     */
    PositionVo getPositionAndParentById(@Param("id") Serializable positionId);

    /**
     * 查询岗位下有效的员工数
     * @param positionId 岗位ID
     * @return
     */
    Integer getEnableEmployeeCount(@Param("positionId") Serializable positionId);

    /**
     * 根据父级岗位ID获取岗位信息
     * @param departmentId 部门ID
     * @param parentId 父级岗位ID
     * @param positionId 岗位id
     * @return
     */
    List<OrganazationVo> getPositionByParentId(@Param("departmentId") Integer departmentId, @Param("parentId") Integer parentId,@Param("positionId") Integer positionId);

    /**
     * 查询企业下的岗位信息
     * @param position 岗位实体
     * @return
     */
    SystemPosition getCompanyPosition(SystemPosition position);

    /**
     * 更新子岗位上级路径
     * @param positionId 岗位ID
     * @param oldParentId 原上级岗位
     * @param newParentId 现上级岗位
     * @return
     */
    int updatePositionPathByParentId(@Param("positionId") Integer positionId, @Param("oldPath") String oldParentId, @Param("newPath") String newParentId);

    /**
     * 去掉子岗位上级路径的最后一个符号
     * @param positionId 岗位ID
     * @return
     */
    int updatePathSeparateByParentId(@Param("positionId") Integer positionId);


    /**
     * 获取上级岗位是上级部门的岗位
     * @param departmentId 部门ID
     * @param positionId 需要过滤的岗位id
     * @return
     */
    List<OrganazationVo> getParentDeptPosition(@Param("departmentId") Integer departmentId,@Param("positionId") Integer positionId);

    /**
     * 根据部门ID及上级部门ID获取上级岗位是上级部门的岗位
     * @param departmentId 部门ID
     * @param parentId 上级部门ID
     * @return
     */
    List<PositionVo> getParentDeptPositionByParentId(@Param("departmentId") Integer departmentId,@Param("parentId") Integer parentId);


    /**
     * 获取本部门及下一级部门的有效岗位列表
     * @param departmentId 部门id
     * @return
     */
    List<OrganazationVo> getPositionAndNextChild(@Param("departmentId") Integer departmentId, @Param("childDeptId") Integer childDeptId,@Param("positionId") Integer positionId);

    /**
     * 获取本部门及下级所有部门的有效岗位列表
     * @param departmentId 部门id
     * @return
     */
    List<OrganazationVo> getPositionAndChilds(@Param("departmentId") Integer departmentId,@Param("positionId") Integer positionId);

    /**
     * 获取所有有效岗位信息
     * @return
     */
    List<OrganazationVo> getPositions();

    /**
     * 获取一级岗位信息
     * @return
     */
    List<OrganazationVo> getRootPosition();

    /**
     * 查询上级岗位是否是该岗位的子级
     * @param positionId 岗位ID
     * @param parentId 上级岗位ID
     * @return
     */
    int getChildPositionFlag(@Param("positionId") Integer positionId,@Param("parentId") Integer parentId);

    /**
     * 禁用子级岗位
     * @param position 岗位实体
     * @return
     */
    int disableChildPositonById(SystemPosition position);
}


package com.fuli.cloud.service;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.dto.PositionDto;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.vo.OrganazationVo;
import com.fuli.cloud.vo.PositionVo;
import io.swagger.models.auth.In;

import java.util.List;

/**
 * 岗位服务接口
 * @author yhm
 * @date 2019/06/25
 */
public interface PositionService {

    /**
     * 查询岗位列表
     * @param positionDto 岗位查询参数
     * @return
     */
    Result getPositionList(PositionDto positionDto);

    /**
     * 根据ID获取岗位信息及父级名称
     * @param positionId 岗位ID
     * @return
     */
    PositionVo getPositionAndParentById(String positionId);

    /**
     * 修改岗位状态
     * @param positionId 岗位ID
     * @param status 状态
     * @param operationAccount 操作人
     * @return
     */
    Result updatePositionStatus(String positionId, Byte status, String operationAccount);

    /**
     * 新增岗位
     * @param position 岗位实体类
     * @return
     */
    Result addPosition(SystemPosition position);

    /**
     * 修改岗位
     * @param position 岗位实体类
     * @return
     */
    Result updatePosition(SystemPosition position);

    /**
     * 获取岗位树
     * @param departmentId 部门ID
     * @param positionId 岗位id
     * @return
     */
    List<OrganazationVo> getPositionTree(Integer departmentId,Integer positionId);

    /**
     * 获取上级岗位树
     * @param departmentId 部门ID
     * @param positionId 岗位id
     * @return
     */
    List<OrganazationVo> getParentPositionTree(Integer departmentId,Integer positionId);

    /**
     * 查询部门岗位树
     * @param departmentIdStr 部门ID
     * @param childDeptId 指定下一级岗位
     * @param showFlag 查询下级岗位标识 0：不查询 1：查询指定下一级岗位 2：查询所有下级岗位
     * @param positionId 需要过滤的岗位id
     * @return
     */
    List<OrganazationVo> getDeptPositionTree(String departmentIdStr, Integer childDeptId, int showFlag,Integer positionId);

    /**
     * 获取整个岗位树
     * @return
     */
    List<OrganazationVo> getAllPositionTree();


    /**
     * <pre>
     * Description: 根据岗位名称查询一个启用的岗位
     * </pre>
     *
     * @param position 岗位名称
     * @return 返回一个启用的岗位Id
     * @author chenyi
     * @date 16:19 2019/8/1
     **/
    SystemPosition getOneByName(String position);


    /**
     * <pre>
     * Description: 检查部门是否属于岗位
     * </pre>
     *
     * @param position 岗位
     * @param deptId   部门id
     * @param count    递归深度计数超过500，停止递归，避免错误数据而引起死循环
     * @return 岗位是否属于部门
     * @author chenyi
     * @date 11:30 2019/8/9
     **/
    boolean checkPositionIsSlaveOfDept(SystemPosition position, Integer deptId, int count);
}

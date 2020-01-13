package com.fuli.cloud.commons.utils;

import com.fuli.auth.common.utils.Md5Utils;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.dto.user.SystemUserSaveDTO;
import com.fuli.cloud.mapper.DepartmentMapper;
import com.fuli.cloud.mapper.PositionMapper;
import com.fuli.cloud.model.SystemDepartment;
import com.fuli.cloud.model.SystemPosition;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.service.SystemUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/28
 */
@Component
public class EmpUtil {

    @Resource
    private PositionService positionService;
    @Resource
    private DepartmentMapper departmentMapper;
    @Resource
    private PositionMapper positionMapper;
    @Resource
    private SystemUserService systemUserService;


    /**
     * <pre>
     * Description: 注意不会生产用户名字段
     * </pre>
     *
     * @author chenyi
     * @date 15:29 2019/8/28
     **/
    public SystemUserDO checkAndFill(SystemUserSaveDTO systemUserSaveDTO, TokenUser tokenUser, Set<String> errorMsg) {

        Integer id = systemUserSaveDTO.getId();

        String certificateCard = systemUserSaveDTO.getCertificateCard();
        if (!PublicUtil.isIDNumber(certificateCard)) {
            errorMsg.add(CodeEnum.SAAS_OPERA_MANAGER_20212020.getMsg());
        }

        // 入职时间不可大于今天
        Date employmentDate = systemUserSaveDTO.getEmploymentDate();
        if (employmentDate != null) {
            if (System.currentTimeMillis() < employmentDate.getTime()) {
                errorMsg.add(CodeEnum.SAAS_OPERA_MANAGER_20212003.getMsg());
            }
        }
        if (!systemUserSaveDTO.isBatchImport()) {
            // 验证部门
            Integer deptId = systemUserSaveDTO.getDeptId();
            SystemDepartment department = null;
            if (deptId != null) {
                department = departmentMapper.selectById(deptId);
                if (department == null || department.getStatus() != 1) {
                    errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212010.getMsg(), deptId));
                }
            }
            // 验证岗位
            Integer positionId = systemUserSaveDTO.getPositionId();
            if (positionId != null) {
                SystemPosition position = positionMapper.selectById(positionId);
                if (null == position || position.getStatus() != 1) {
                    errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212011.getMsg(), positionId));
                } else {
                    // 快速验证岗位是否属于部门
                    if (department != null) {
                        if (!positionService.checkPositionIsSlaveOfDept(position, department.getId(), 0)) {
                            errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212019.getMsg(), position.getName(), department.getName()));
                        }
                    }
                }
            }
            // 验证角色
            Set<Integer> roleIds = systemUserSaveDTO.getRoleIds();
            if (systemUserSaveDTO.getOpenSystem() == 1) {
                if (CollectionUtils.isEmpty(roleIds)) {
                    errorMsg.add(CodeEnum.SAAS_OPERA_MANAGER_20212007.getMsg());
                }
            }
        }

        SystemUserDO systemUserDO = null;
        String phoneNumber = systemUserSaveDTO.getPhoneNumber();
        if (null != id) {
            systemUserDO = systemUserService.getById(id);
            if (systemUserSaveDTO.getSaveType() == 0) {
                if (systemUserDO == null) {
                    errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212017.getMsg(), systemUserSaveDTO.getSaveType()));
                } else {
                    if (systemUserDO.isInService()) {
                        errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212021.getMsg(), systemUserSaveDTO.getSaveType()));
                    }
                }
            }
            if (systemUserDO == null) {
                errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), id));
            } else {
                // 离职的员工在入职，请走离职后在入职流程
                if (!systemUserDO.isInService() && systemUserSaveDTO.getSaveType() == 0) {
                    errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212006.getMsg(), systemUserSaveDTO.getSaveType(), 1));
                }
                // 离职的员工不能编辑
                if (!systemUserDO.isInService() && systemUserSaveDTO.getSaveType() == 2) {
                    errorMsg.add(CodeEnum.SAAS_OPERA_MANAGER_20212002.getMsg());
                }
                systemUserDO.setStatus(1);
                // 在职 1.验证身份证
                if (StringUtils.isNotBlank(certificateCard)) {
                    String oldIdCard = systemUserDO.getCertificateCard();
                    if (!oldIdCard.equals(certificateCard)) {
                        if (systemUserService.exsitByCertificateCard(certificateCard)) {
                            errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212004.getMsg(), certificateCard));
                        }
                    }
                }
                // 2.验证电话号码
                if (StringUtils.isNotBlank(phoneNumber)) {
                    String oldPhoneNumber = systemUserDO.getPhoneNumber();
                    if (!oldPhoneNumber.equals(phoneNumber)) {
                        if (systemUserService.exsitByPhoneNumber(phoneNumber)) {
                            errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212005.getMsg(), phoneNumber));
                        }
                    }
                }
                BeanUtils.copyProperties(systemUserSaveDTO, systemUserDO);
                systemUserDO.setUpdateTime(new Date());
                systemUserDO.setOperationAccount(tokenUser.getUserAccount());
            }
        } else {
            if (systemUserSaveDTO.getSaveType() != 0) {
                errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212017.getMsg(), systemUserSaveDTO.getSaveType()));
            }
            // 新增 1.验证身份证
            if (StringUtils.isNotBlank(certificateCard)) {
                SystemUserDO oneByCertificateCard = systemUserService.getOneByCertificateCard(certificateCard);
                if (null != oneByCertificateCard) {
                    if (oneByCertificateCard.isInService()) {
                        errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212004.getMsg(), certificateCard));
                    } else {
                        // 离职员工中存在
                        errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212018.getMsg(), certificateCard));
                    }
                }
            }
            //2.验证电话号码
            if (StringUtils.isNotBlank(phoneNumber)) {
                if (systemUserService.exsitByPhoneNumber(phoneNumber)) {
                    errorMsg.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212005.getMsg(), phoneNumber));
                }
            }
            systemUserDO = new SystemUserDO();
            BeanUtils.copyProperties(systemUserSaveDTO, systemUserDO);
            systemUserDO.setCreateTime(new Date());
            systemUserDO.setCreateOperator(tokenUser.getUserAccount());
            // 生成用户名
//            String userName = SpellHelperUtil.toPinyin(systemUserSaveDTO.getName());
//            //如遇系统有相同的用户名，则在用户名后面加数字区分。比如系统已经有zhangsan，本次导入一个同名或同音的人时，生成用户名zhangsan2
//            userName = systemUserService.genarateUserName(userName, 1, userName);
//            systemUserDO.setUsername(userName);
            systemUserDO.setCreateOpId(tokenUser.getId());
        }
        if (!errorMsg.isEmpty()) {
            return null;
        }
        if (systemUserDO == null) {
            return null;
        }
        // 编辑操作是不需要设置默认密码
        if (systemUserSaveDTO.getSaveType() != 2) {
            // 设置默认密码
            systemUserDO.setPassword(Md5Utils.encode(Md5Utils.DEFAULT_PASSWORD));
        }
        if (systemUserSaveDTO.getOpenSystem() == 0) {
            systemUserSaveDTO.setRoleIds(null);
        } else {
            systemUserDO.setRoleIds(systemUserSaveDTO.getRoleIds());
        }
        systemUserDO.setOpId(tokenUser.getId());
        systemUserDO.setStatus(1);
        systemUserDO.setGender(PublicUtil.getSex(systemUserDO.getCertificateCard()));
        return systemUserDO;
    }

}

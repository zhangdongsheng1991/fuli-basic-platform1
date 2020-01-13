package com.fuli.cloud.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.EmpUtil;
import com.fuli.cloud.commons.utils.QWrapper;
import com.fuli.cloud.commons.utils.SpellHelperUtil;
import com.fuli.cloud.dto.*;
import com.fuli.cloud.dto.user.SystemUserEditOrViewQueryDTO;
import com.fuli.cloud.dto.user.SystemUserSaveDTO;
import com.fuli.cloud.dto.user.SystemUserSearchDTO;
import com.fuli.cloud.mapper.SystemRoleMapper;
import com.fuli.cloud.mapper.SystemUserMapper;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.service.SystemUserService;
import com.fuli.cloud.vo.*;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;


/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/7/29
 */
@Service
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUserDO> implements SystemUserService {

    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    SystemRoleMapper systemRoleMapper;

    @Resource
    private EmpUtil empUtil;

    @Override
    public SystemUserDO getOneByCertificateCard(String certificateCard) {

        if (StringUtils.isBlank(certificateCard)) {
            return null;
        }
        QWrapper<SystemUserDO> qWrapper = new QWrapper<>();
        qWrapper.eq(SystemUserDO.Fields.certificateCard, certificateCard).last("limit 1");
        return baseMapper.selectOne(qWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveOne(SystemUserSaveDTO systemUserSaveDTO, TokenUser tokenUser) {

        Set<String> errorMsg = Sets.newHashSet();
        SystemUserDO systemUserDO = empUtil.checkAndFill(systemUserSaveDTO, tokenUser, errorMsg);
        if (!errorMsg.isEmpty()) {
            throw new ServiceException(-1, StringUtils.join(errorMsg, ";"));
        }
        if (systemUserDO.getId() == null) {
            //生成用户名
            String userName = SpellHelperUtil.toPinyin(systemUserDO.getName());
            if (StringUtils.isNotBlank(userName)){
                //如遇系统有相同的用户名，则在用户名后面加数字区分。比如系统已经有zhangsan，本次导入一个同名或同音的人时，生成用户名zhangsan2
                userName = genarateUserName(userName, 1, userName);
                systemUserDO.setUsername(userName);
            }
        }
        if (saveOrUpdate(systemUserDO)) {
            // 插入用户角色关联表
            systemRoleService.saveOrUpdateRoleUserByUserId(systemUserDO.getId(), systemUserDO.getRoleIds());
            return true;
        }
        return false;
    }


    /**
     * <pre>
     * Description: 如遇系统有相同的用户名，则在用户名后面加数字区分。
     *  比如系统已经有zhangsan，本次导入一个同名或同音的人时，生成用户名zhangsan2
     * </pre>
     *
     * @param userName 拼音用户名称
     * @param postFix  后缀
     * @return 系统用户名
     * @author chenyi
     * @date 17:35 2019/8/1
     **/
    private String genarateUserName(String initUserName, int postFix, String userName) {

        QWrapper<SystemUserDO> qWrapper = new QWrapper<>();
        qWrapper.eq(SystemUserDO.Fields.username, userName);

        Integer count = this.baseMapper.selectCount(qWrapper);
        if (count == 0) {
            return userName;
        }
        postFix++;
        return genarateUserName(initUserName, postFix, initUserName + postFix);
    }

    @Override
    public List<SystemUserEditOrViewVO> getSystemUserEditOrViewVOList(Set<Integer> ids) {

        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        SystemUserEditOrViewQueryDTO systemUserEditOrViewQueryDTO = new SystemUserEditOrViewQueryDTO();
        systemUserEditOrViewQueryDTO.setIdsStr(StringUtils.join(ids, ","));
        List<SystemUserEditOrViewVO> voList = this.baseMapper.getSystemUserEditOrViewVOList(systemUserEditOrViewQueryDTO);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }
        for (SystemUserEditOrViewVO systemUserEditOrViewVO : voList) {
            // 查询角色
            List<RoleIdNameVo> roleIdNameVoList = systemRoleMapper.getRoleNamesByUserId(systemUserEditOrViewVO.getId());
            systemUserEditOrViewVO.setRoleIdNames(roleIdNameVoList);
        }
        return voList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveRoleInfo(SystemUserDO systemUserDO, RoleInfoDTO roleInfoDto, TokenUser tokenUser) {

        if (null == systemUserDO) {
            return false;
        }
        if (roleInfoDto.getOpenSystem() == 1) {
            if (CollectionUtils.isEmpty(roleInfoDto.getRoleIds())) {
                throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212007);
            }
        }
        // 离职不支持授权操作
        if (systemUserDO.getStatus() != 1) {
            throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212012, systemUserDO.getId(), systemUserDO.getPhoneNumber());
        }
        Integer openSystem = roleInfoDto.getOpenSystem();
        SystemUserDO updateVO = new SystemUserDO();
        updateVO.setId(systemUserDO.getId());
        if (systemUserDO.getOpenSystem().intValue() != openSystem.intValue()) {
            updateVO.setOpenSystem(openSystem);
        }
        updateVO.setUpdateTime(new Date());
        updateVO.setOpId(tokenUser.getId());
        updateVO.setOperationAccount(tokenUser.getUserAccount());
        if (updateById(updateVO)) {
            systemRoleService.saveOrUpdateRoleUserByUserId(systemUserDO.getId(), roleInfoDto.getRoleIds());
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean dimission(SystemUserDO systemUserDO, TokenUser tokenUser) {
        if (systemUserDO == null) {
            return false;
        }
        // 已离职不能再次离职
        if (!systemUserDO.isInService()) {
            throw new ServiceException(CodeEnum.SAAS_OPERA_MANAGER_20212013, systemUserDO.getId(), systemUserDO.getPhoneNumber());
        }
        SystemUserDO update = new SystemUserDO();
        update.setId(systemUserDO.getId());
        update.dimission();
        update.setOperationAccount(tokenUser.getUserAccount());
        update.setOpId(tokenUser.getId());
        update.setUpdateTime(new Date());
        // 离职后清空角色信息
        if (updateById(update)) {
            systemRoleService.saveOrUpdateRoleUserByUserId(systemUserDO.getId(), null);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<SystemUserListVO> listOnePage(SystemUserSearchDTO systemUserSearchDTO) {

        PageHelper.startPage(systemUserSearchDTO.getPageNum(), systemUserSearchDTO.getPageSize());
        List<SystemUserListVO> onePage = this.baseMapper.listOnePage(systemUserSearchDTO);
        if (CollectionUtils.isEmpty(onePage)) {
            return Collections.emptyList();
        }
        Map<Integer, SystemUserListVO> userIdToEntity = Maps.newHashMap();
        for (SystemUserListVO systemUserListVO : onePage) {
            userIdToEntity.put(systemUserListVO.getId(), systemUserListVO);
        }
        // 平台角色
        int batchSize = 1000;
        int count = 0;
        Set<Integer> userIds = Sets.newHashSet();
        for (SystemUserListVO systemUserListVO : onePage) {
            if (count < batchSize) {
                count++;
                userIds.add(systemUserListVO.getId());
                continue;
            }
            // 设置角色名称
            setListRoleNamesStr(userIdToEntity, userIds);
            count = 0;
            userIds.clear();
        }
        if (count != 0 && count < batchSize) {
            setListRoleNamesStr(userIdToEntity, userIds);
        }
        return onePage;
    }

    /**
     * <pre>
     * Description: 设置角色名称
     * </pre>
     *
     * @author chenyi
     * @date 17:35 2019/8/5
     **/
    private void setListRoleNamesStr(Map<Integer, SystemUserListVO> userIdToEntity, Set<Integer> userIds) {

        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<RoleNameVo> roleNameVoList = this.systemRoleService.getRoleNameUserIdList(StringUtils.join(userIds, ","));

        // 根据用户id分拣
        Map<Integer, Set<String>> userIdToRoleNames = Maps.newHashMap();
        for (RoleNameVo vo : roleNameVoList) {
            Integer userId = vo.getUserId();
            Set<String> set = userIdToRoleNames.computeIfAbsent(userId, k -> Sets.newHashSet());
            String roleName = vo.getRoleName();
            if (StringUtils.isNotBlank(roleName)) {
                set.add(roleName);
            }
        }

        for (Map.Entry<Integer, Set<String>> en : userIdToRoleNames.entrySet()) {
            Integer userId = en.getKey();
            Set<String> value = en.getValue();
            if (CollectionUtils.isEmpty(value)) {
                continue;
            }
            SystemUserListVO sysUserExportVO = userIdToEntity.get(userId);
            if (sysUserExportVO != null) {
                sysUserExportVO.setSaasRoleNamesStr(StringUtils.join(value, ","));
            }
        }

    }

    @Override
    public List<SysUserExportVO> getSysUserExportVOList(SystemUserSearchDTO systemUserSearchDTO) {
        List<SysUserExportVO> sysUserExportVOList = this.baseMapper.getSysUserExportVOList(systemUserSearchDTO);
        if (CollectionUtils.isEmpty(sysUserExportVOList)) {
            return Collections.emptyList();
        }
        Map<Integer, SysUserExportVO> userIdToEntity = Maps.newHashMap();
        for (SysUserExportVO sysUserExportVO : sysUserExportVOList) {
            userIdToEntity.put(sysUserExportVO.getId(), sysUserExportVO);
        }
        // 平台角色
        int batchSize = 1000;
        int count = 0;
        Set<Integer> userIds = Sets.newHashSet();
        for (SysUserExportVO sysUserExportVO : sysUserExportVOList) {
            if (count < batchSize) {
                count++;
                userIds.add(sysUserExportVO.getId());
                continue;
            }
            // 设置角色名称
            setRoleNamesStr(userIdToEntity, userIds);
            count = 0;
            userIds.clear();
        }
        if (count != 0 && count < batchSize) {
            setRoleNamesStr(userIdToEntity, userIds);
        }
        return sysUserExportVOList;
    }

    @Override
    public SystemUserEditOrViewVO getOneSystemUserEditOrViewVO(String certificateCard) {
        if (StringUtils.isBlank(certificateCard)) {
            return null;
        }
        SystemUserEditOrViewQueryDTO systemUserEditOrViewQueryDTO = new SystemUserEditOrViewQueryDTO();
        systemUserEditOrViewQueryDTO.setCertificateCard(certificateCard);
        List<SystemUserEditOrViewVO> viewVOList = this.baseMapper.getSystemUserEditOrViewVOList(systemUserEditOrViewQueryDTO);
        if (null == viewVOList || viewVOList.isEmpty()) {
            return null;
        }
        // 查询角色
        SystemUserEditOrViewVO systemUserEditOrViewVO = viewVOList.get(0);
        List<RoleIdNameVo> roleIdNameVoList = systemRoleMapper.getRoleNamesByUserId(systemUserEditOrViewVO.getId());
        systemUserEditOrViewVO.setRoleIdNames(roleIdNameVoList);
        return systemUserEditOrViewVO;
    }


    /**
     * <pre>
     * Description: 设置角色名称
     * </pre>
     *
     * @author chenyi
     * @date 17:35 2019/8/5
     **/
    private void setRoleNamesStr(Map<Integer, SysUserExportVO> userIdToEntity, Set<Integer> userIds) {

        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<RoleNameVo> roleNameVoList = this.systemRoleService.getRoleNameUserIdList(StringUtils.join(userIds, ","));

        // 根据用户id分拣
        Map<Integer, Set<String>> userIdToRoleNames = Maps.newHashMap();
        for (RoleNameVo vo : roleNameVoList) {
            Integer userId = vo.getUserId();
            Set<String> set = userIdToRoleNames.computeIfAbsent(userId, k -> Sets.newHashSet());
            String roleName = vo.getRoleName();
            if (StringUtils.isNotBlank(roleName)) {
                set.add(roleName);
            }
        }

        for (Map.Entry<Integer, Set<String>> en : userIdToRoleNames.entrySet()) {
            Integer userId = en.getKey();
            Set<String> value = en.getValue();
            if (CollectionUtils.isEmpty(value)) {
                continue;
            }
            SysUserExportVO sysUserExportVO = userIdToEntity.get(userId);
            if (sysUserExportVO != null) {
                sysUserExportVO.setSaasRoleNamesStr(StringUtils.join(value, ","));
            }
        }

    }

    @Override
    public int statisticsEmpInCount(EmpCountDTO empCountDTO) {
        empCountDTO.setStatus(1);
        return this.baseMapper.statisticsEmpCount(empCountDTO);
    }

    @Override
    public int statisticsEmpOutCount(EmpCountDTO empCountDTO) {
        empCountDTO.setStatus(0);
        return this.baseMapper.statisticsEmpCount(empCountDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveList(List<SystemUserDO> toSaveList) {

        Map<String, Integer> nameToNum = Maps.newHashMapWithExpectedSize(toSaveList.size());

        for (SystemUserDO systemUserDO : toSaveList) {
            if (systemUserDO.getId() == null) {
                //生成用户名
                String userName = SpellHelperUtil.toPinyin(systemUserDO.getName());
                if (StringUtils.isNotBlank(userName)){
                    Integer num = nameToNum.get(userName);
                    String intUname = userName;
                    if (num != null) {
                        intUname = userName + (num + 1);
                    }
                    //如遇系统有相同的用户名，则在用户名后面加数字区分。比如系统已经有zhangsan，本次导入一个同名或同音的人时，生成用户名zhangsan2
                    String targetUname = genarateUserName(userName, 1, intUname);
                    if (!targetUname.equals(userName)) {
                        num = Integer.valueOf(targetUname.replaceAll(userName, ""));
                    } else {
                        num = 1;
                    }
                    nameToNum.put(userName, num);
                    systemUserDO.setUsername(targetUname);
                }
            }
            if (saveOrUpdate(systemUserDO)) {
                // 插入用户角色关联表
                systemRoleService.saveOrUpdateRoleUserByUserId(systemUserDO.getId(), systemUserDO.getRoleIds());
            }
        }
    }

    /**
     * 根据手机号查询用户信息
     *
     * @param phone : 手机号
     * @return Result
     * @author WFZ
     * @date 2019/7/31 9:30
     */
    @Override
    public SystemUserDO getOneByPhone(String phone) {
        return getOneByPhoneNumber(phone);
    }

    @Override
    public boolean exsitByPhoneNumber(String phoneNumber) {
        return null != getOneByPhoneNumber(phoneNumber);
    }

    @Override
    public boolean exsitByCertificateCard(String certificateCard) {
        return null != getOneByCertificateCard(certificateCard);
    }

    private SystemUserDO getOneByPhoneNumber(String phoneNumber) {
        if (StringUtils.isBlank(phoneNumber)) {
            return null;
        }
        QWrapper<SystemUserDO> qWrapper = new QWrapper<>();
        qWrapper.eq(SystemUserDO.Fields.phoneNumber, phoneNumber).last("limit 1");
        return baseMapper.selectOne(qWrapper);
    }
}

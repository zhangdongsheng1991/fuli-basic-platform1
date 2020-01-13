package com.fuli.cloud.commons.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.dto.user.SystemUserImportDTO;
import com.fuli.cloud.dto.user.SystemUserSaveDTO;
import com.fuli.cloud.model.*;
import com.fuli.cloud.service.DepartmentService;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.service.SystemUserService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author chenyi
 * @date 2019/7/31
 */
@Slf4j
public class SystemUserImportListener extends AnalysisEventListener<SystemUserImportDTO> {

    private SystemUserService systemUserService;

    private DepartmentService departmentService;

    private PositionService positionService;

    private SystemRoleService systemRoleService;

    private EmpUtil empUtil;

    private List<String> errorList = new LinkedList<>();
    private List<SystemUserDO> toSaveList = new LinkedList<>();

    private Set<String> idCardSet = Sets.newHashSet();
    private Set<String> phoneSet = Sets.newHashSet();

    public List<String> getErrorList() {
        return errorList;
    }

    public List<SystemUserDO> getToSaveList() {
        return toSaveList;
    }

    public SystemUserImportListener(SystemUserService systemUserService, DepartmentService departmentService,
                                    PositionService positionService, SystemRoleService systemRoleService, EmpUtil empUtil) {
        this.systemUserService = systemUserService;
        this.departmentService = departmentService;
        this.positionService = positionService;
        this.systemRoleService = systemRoleService;
        this.empUtil = empUtil;
    }

    public SystemUserImportListener() {
    }

    @Override
    public void invoke(SystemUserImportDTO systemUserImportDTO, AnalysisContext context) {

        Integer currentRowNum = context.getCurrentRowNum();
        if (currentRowNum == 0) {
            checkSystemUserImportTitle(systemUserImportDTO);
            return;
        }
        try {
            Set<String> errorMsgs = doCheckAndFill(systemUserImportDTO, context);
            if (!CollectionUtils.isEmpty(errorMsgs)) {
                addErrorMsg(systemUserImportDTO, context, StringUtils.join(errorMsgs, "；"));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Throwable cause = e.getCause();
            String errorMsg = e.getMessage();
            if (cause != null) {
                errorMsg = cause.getMessage();
            }
            addErrorMsg(systemUserImportDTO, context, errorMsg);
        }
        JacksonUtil.dumnToPrettyJsonDebug("读取一行[" + currentRowNum + "]Excel数据： ", systemUserImportDTO);
    }

    private static final String ROLE_SPLITOR = "/";

    private Set<String> doCheckAndFill(SystemUserImportDTO systemUserImportDTO, AnalysisContext context) {

        Set<String> errorMsgs = Sets.newLinkedHashSet();

        // 去除String的前后空格
        PublicUtil.trim(systemUserImportDTO);
        // 跳过空行
        if (isBlankLine(systemUserImportDTO)) {
            return Collections.emptySet();
        }
        systemUserImportDTO.setCertificateCard(systemUserImportDTO.getCertificateCard().toUpperCase());
        Set<String> validateRetureSet = VlidationUtil.validateRetureSet(systemUserImportDTO);
        if (!CollectionUtils.isEmpty(validateRetureSet)) {
            errorMsgs.addAll(validateRetureSet);
        }
        // 校验数据
        String role = systemUserImportDTO.getRole();
        if (StringUtils.isBlank(role)) {
            systemUserImportDTO.setRole(null);
        }
        SystemUserSaveDTO systemUserSaveDTO = newSystemUserSaveDTO(context, systemUserImportDTO, errorMsgs);
        TokenUser tokenUser = (TokenUser) context.getCustom();
        SystemUserDO systemUserDO = empUtil.checkAndFill(systemUserSaveDTO, tokenUser, errorMsgs);

        if (this.idCardSet.contains(systemUserSaveDTO.getCertificateCard())) {
            errorMsgs.add("表格中的身份证[" + systemUserSaveDTO.getCertificateCard() + "]重复");
        }
        if (this.phoneSet.contains(systemUserSaveDTO.getPhoneNumber())) {
            errorMsgs.add("表格中的手机号[" + systemUserSaveDTO.getPhoneNumber() + "]重复");
        }
        if (!errorMsgs.isEmpty()) {
            return errorMsgs;
        }
        if (systemUserDO != null) {
            this.toSaveList.add(systemUserDO);
        }
        this.phoneSet.add(systemUserSaveDTO.getPhoneNumber());
        this.idCardSet.add(systemUserSaveDTO.getCertificateCard());
        return null;
    }

    private boolean isBlankLine(SystemUserImportDTO o) {

        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            // 跳过静态字段
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (f.getType() == String.class) {
                try {
                    String value = (String) f.get(o);
                    if (StringUtils.isNotBlank(value)) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("====> blank line ");
        return true;
    }

    private SystemUserSaveDTO newSystemUserSaveDTO(AnalysisContext context, SystemUserImportDTO systemUserImportDTO, Set<String> errorMsgs) {

        String depName = systemUserImportDTO.getDepName();
        SystemDepartment detp = null;
        if (StringUtils.isNotBlank(depName)) {
            detp = departmentService.getOneByName(depName);
            if (detp == null) {
                errorMsgs.add("部门[" + depName + "]不存在");
            }
        }

        String position1 = systemUserImportDTO.getPosition();
        SystemPosition position = null;
        if (StringUtils.isNotBlank(position1)) {
            position = this.positionService.getOneByName(position1);
            if (position == null) {
                errorMsgs.add("岗位[" + position1 + "]不存在");
            } else {
                if (detp != null) {
                    if (!positionService.checkPositionIsSlaveOfDept(position, detp.getId(), 0)) {
                        errorMsgs.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212019.getMsg(), position.getName(), detp.getName()));
                    }
                }
            }
        }
        String employmentDateStr = systemUserImportDTO.getEmploymentDate();
        Date employmentDate = null;
        if (StringUtils.isNotBlank(employmentDateStr)) {
            int days;
            try {
                //使用 EasyExcel 读取 Excel 数据时，表格中的日期自动转化为了一串数字
                //因为 Excel 导入的时间是以1900 年为原点的，而数字 34839 和 36577 则是1995/5/20 和 2000/2/21 与 1900 年之间经过的天数差值。
                days = Integer.valueOf(employmentDateStr);
                Calendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, -1);
                calendar.add(Calendar.DATE, days);
                employmentDate = calendar.getTime();
                if (new Date().before(employmentDate)) {
                    errorMsgs.add("入职时间[" + DateUtil.formatDate(employmentDate) + "]不能在当前时间之前");
                }
            } catch (Exception e) {
                log.info("格式化入职日期出错: " + e.getMessage(), e);
                errorMsgs.add("入职时间[" + employmentDateStr + "]格式不正确");
            }
        }
        String role = systemUserImportDTO.getRole();
        // 不开通权限
        int openSystem = 0;
        Set<Integer> roleIds = Sets.newHashSet();
        if (StringUtils.isNotBlank(role)) {
            openSystem = 1;
            Set<String> roleNames = Sets.newHashSet();
            if (!role.contains(ROLE_SPLITOR)) {
                roleNames.add(role);
            } else {
                String[] roles = role.split(ROLE_SPLITOR);
                for (String r : roles) {
                    if (StringUtils.isNotBlank(r)) {
                        roleNames.add(r);
                    }
                }
            }
            if (roleNames.isEmpty()) {
                errorMsgs.add("平台角色[" + role + "]填写错误");
                openSystem = 0;
            } else {
                QWrapper<SystemRoleDO> qWrapper = new QWrapper<>();
                qWrapper.select(SystemRoleDO.Fields.id, SystemRoleDO.Fields.name, SystemRoleDO.Fields.administrators)
                        .eq(SystemRoleDO.Fields.status, 1)
                        .in(SystemRoleDO.Fields.name, roleNames);
                List<SystemRoleDO> systemRoleDOS = this.systemRoleService.list(qWrapper);
                if (CollectionUtils.isEmpty(systemRoleDOS)) {
                    errorMsgs.add("平台角色[" + StringUtils.join(roleNames, ROLE_SPLITOR) + "]不存在");
                    openSystem = 0;
                } else {
                    for (SystemRoleDO systemRoleDO : systemRoleDOS) {
                        if (systemRoleDO.getAdministrators() == 1) {
                            errorMsgs.add("平台角色[" + systemRoleDO.getName() + "]是超级管理员，不能赋予员工");
                            openSystem = 0;
                        }
                        roleIds.add(systemRoleDO.getId());

                        roleNames.remove(systemRoleDO.getName());
                    }
                    if (!roleNames.isEmpty()) {
                        errorMsgs.add("平台角色[" + StringUtils.join(roleNames, ROLE_SPLITOR) + "]不存在");
                        openSystem = 0;
                    }
                }
            }
        }
        Integer userId = null;
        int saveType = 0;
        String certificateCard = systemUserImportDTO.getCertificateCard();
        String phoneNumber = systemUserImportDTO.getPhoneNumber();
        SystemUserDO oneByCertificateCard = this.systemUserService.getOneByCertificateCard(certificateCard);
        String name = systemUserImportDTO.getName();
        if (null != oneByCertificateCard) {
            userId = oneByCertificateCard.getId();
            if (oneByCertificateCard.isInService()) {
                errorMsgs.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212004.getMsg(), certificateCard));
            }
            saveType = 1;
        }
        SystemUserDO oneByPhone = this.systemUserService.getOneByPhone(phoneNumber);
        if (oneByPhone != null) {
            userId = oneByPhone.getId();
            if (oneByPhone.isInService()) {
                errorMsgs.add(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212005.getMsg(), phoneNumber));
            }
            saveType = 1;
        }

        SystemUserSaveDTO systemUserSaveDTO = new SystemUserSaveDTO();
        systemUserSaveDTO.setId(userId);
        systemUserSaveDTO.setName(name);
        systemUserSaveDTO.setCertificateCard(certificateCard);
        systemUserSaveDTO.setPhoneNumber(phoneNumber);
        systemUserSaveDTO.setDeptId(detp == null ? null : detp.getId());
        systemUserSaveDTO.setPositionId(position == null ? null : position.getId());
        systemUserSaveDTO.setEmploymentDate(employmentDate);
        systemUserSaveDTO.setOpenSystem(openSystem);
        systemUserSaveDTO.setRoleIds(roleIds);
        systemUserSaveDTO.setSaveType(saveType);
        systemUserSaveDTO.setBatchImport(true);
        JacksonUtil.dumnToPrettyJson(systemUserSaveDTO);
        return systemUserSaveDTO;
    }

    private void addErrorMsg(SystemUserImportDTO systemUserImportDTO, AnalysisContext context, String msg) {
        String name = systemUserImportDTO.getName();
        if (name == null) {
            name = "";
        }
        String errorMsg = "第" + (context.getCurrentRowNum() + 1) + "行：" + name + "，失败原因“" + msg + "”";
        log.info(errorMsg);
        this.errorList.add(errorMsg);
    }

    //姓名	身份证号	手机号码	部门	岗位	入职时间	SaaS平台角色
    private static final Set<String> excelTitle = Sets.newHashSet("姓名", "身份证号", "手机号码", "部门", "岗位", "入职时间", "SaaS平台角色");

    private void checkSystemUserImportTitle(SystemUserImportDTO systemUserImportDTO) {
        try {
            String json = JacksonUtil.toJson(systemUserImportDTO);
            Map map = JacksonUtil.parseJsonWithFormat(json, Map.class);
            // 从BaseRowModel继承来的一个属性
            map.remove("cellStyleMap");
            Collection values = map.values();
            for (Object val : values) {
                System.out.println(val);

                if (null == val || !excelTitle.contains(val.toString().trim())) {
                    throw new RuntimeException("导入文件格式错误，请下载最新的导入模板导入数据");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("导入文件格式错误，请下载最新的导入模板导入数据.IOException", e);
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }


}
package com.fuli.cloud.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectResult;
import com.fuli.cloud.commons.CodeEnum;
import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.Result;
import com.fuli.cloud.commons.base.BaseController;
import com.fuli.cloud.commons.exception.ServiceException;
import com.fuli.cloud.commons.utils.*;
import com.fuli.cloud.config.AliyunOSSConfig;
import com.fuli.cloud.dto.EmpCountDTO;
import com.fuli.cloud.dto.RoleInfoDTO;
import com.fuli.cloud.dto.UserIdDTO;
import com.fuli.cloud.dto.user.SystemUserImportDTO;
import com.fuli.cloud.dto.user.SystemUserSaveDTO;
import com.fuli.cloud.dto.user.SystemUserSearchDTO;
import com.fuli.cloud.model.SystemUserDO;
import com.fuli.cloud.model.TokenUser;
import com.fuli.cloud.service.DepartmentService;
import com.fuli.cloud.service.PositionService;
import com.fuli.cloud.service.SystemRoleService;
import com.fuli.cloud.service.SystemUserService;
import com.fuli.cloud.vo.*;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户表
 *
 * @author chenyi 2019-07-29
 */
@Api(tags = "员工管理 - 陈毅")
@RestController
@RequestMapping("/systemUser")
@Validated
@Slf4j
public class SystemUserController extends BaseController {

    @Resource
    private SystemUserService systemUserService;
    @Resource
    private SystemRoleService systemRoleService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private PositionService positionService;
    @Resource
    private EmpUtil empUtil;

    @PostMapping("/save")
    @ApiOperation(value = "保存员工（入职/编辑）")
    public Result save(@RequestBody @Validated SystemUserSaveDTO systemUserSaveDTO) {
        JacksonUtil.dumnToPrettyJsonInfo("保存员工: ", systemUserSaveDTO);
        PublicUtil.trim(systemUserSaveDTO);
        systemUserSaveDTO.setCertificateCard(systemUserSaveDTO.getCertificateCard().toUpperCase());
        return systemUserService.saveOne(systemUserSaveDTO, this.getSystemUser()) ? Result.succeed() : Result.failed();
    }

    @GetMapping("/validPhone")
    @ApiOperation(value = "（新增或者编辑）异步校验手机号的唯一性接口")
    public Result validPhone(@Valid @NotBlank(message = "手机号不能为空") @RequestParam("phone") String phone,
                             @RequestParam(value = "id", required = false) Integer id) {

        QWrapper<SystemUserDO> query = new QWrapper<>();
        query.select(SystemUserDO.Fields.id)
                .eq(SystemUserDO.Fields.phoneNumber, phone)
                .eq(SystemUserDO.Fields.status, 1);
        SystemUserDO one = this.systemUserService.getOne(query);
        if (id != null) {
            if (one != null) {
                if (id.intValue() != one.getId().intValue()) {
                    return Result.failed("该手机号已存在，请重新输入");
                }
            }
        } else {
            if (one != null) {
                return Result.failed("该手机号已存在，请重新输入");
            }
        }
        return Result.succeed();

    }


    @GetMapping("/getOneByCertificateCard")
    @ApiOperation(value = "（新增或者编辑）异步校验身份证号码的唯一性接口")
    public Result<SystemUserEditOrViewVO> getOneByCertificateCard(@Valid @NotBlank(message = "身份证号不能为空") @RequestParam("certificateCard") String certificateCard,
                                                                  @RequestParam(value = "id", required = false) Integer id) {
        // id为空，新增校验
        SystemUserEditOrViewVO viewVO = systemUserService.getOneSystemUserEditOrViewVO(certificateCard);
        if (id == null) {
            if (viewVO != null && viewVO.getStatus() == 1) {
                return Result.failed("该身份证号对应员工已存在，请重新输入");
            }
        } else {
            // id非空，编辑校验
            if (viewVO != null && viewVO.getId().intValue() != id.intValue()) {
                return Result.failed("该身份证号对应员工已存在，请重新输入");
            }
        }

        JacksonUtil.dumnToPrettyJsonInfo("根据身份证查询员工: ", certificateCard);

        if (null == viewVO) {
            return Result.succeed();
        }

        // 全部启用的角色信息
        List<RoleIdNameVo> allRoleIdNames = this.systemRoleService.listRoleIdNameEnable();
        // 全部启用的部门树
        List<OrganazationVo> departmentList = this.departmentService.getDepartmentTree();
        viewVO.setAllRoleIdNames(allRoleIdNames);
        viewVO.setDepartmentList(departmentList);
        // 全部启用的岗位树
        List<OrganazationVo> positionList = this.positionService.getDeptPositionTree(viewVO.getDeptId().toString(), 0, 2,null);
        viewVO.setPositionList(positionList);

        return Result.succeed(viewVO);
    }


    @PostMapping("/statisticsEmpInAndOutCount")
    @ApiOperation(value = "统计在职和离职员工的人数")
    public Result<EmpInAndOutCountVO> statisticsEmpInAndOutCount() {

        SystemUserDO byId = this.systemUserService.getById(this.getSystemUserId());
        if (null == byId) {
            throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
        }

        /*获取当前用户的部门id与岗位id*/
        EmpCountDTO empCountDTO = new EmpCountDTO();
        empCountDTO.setDeptId(byId.getDeptId());
        empCountDTO.setPositionId(byId.getPositionId());
        empCountDTO.setId(this.getSystemUserId());
        empCountDTO.setFirtSecoundOfMonth(DateUtil.firtSecoundOfMonth());
        empCountDTO.setLastSecoundOfMonth(DateUtil.lastSecoundOfMonth());

        int inCount = this.systemUserService.statisticsEmpInCount(empCountDTO);
        int outCount = this.systemUserService.statisticsEmpOutCount(empCountDTO);

        EmpInAndOutCountVO empInAndOutCountVO = new EmpInAndOutCountVO(inCount, outCount);
        // 全部启用的角色信息
        return Result.succeed(empInAndOutCountVO);
    }

    @PostMapping("/getAllRoleIdNames")
    @ApiOperation(value = "全部启用的角色id和名称")
    public Result<List<RoleIdNameVo>> getAllRoleIdNames() {
        // 全部启用的角色信息
        return Result.succeed(this.systemRoleService.listRoleIdNameEnable());
    }


    @PostMapping("/getListByIds")
    @ApiOperation(value = "根据id集合查询员工")
    public Result<List<SystemUserEditOrViewVO>> getListByIds(@RequestBody @Validated UserIdDTO userIdDTO) {

        JacksonUtil.dumnToPrettyJsonInfo("根据id查询员工: ", userIdDTO);

        List<SystemUserEditOrViewVO> systemUserByIdVOs = systemUserService.getSystemUserEditOrViewVOList(userIdDTO.getIds());
        if (CollectionUtils.isEmpty(systemUserByIdVOs)) {
            return Result.failed(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), StringUtils.join(userIdDTO.getIds(), ",")));
        }
        // 全部启用的角色信息
        List<RoleIdNameVo> allRoleIdNames = this.systemRoleService.listRoleIdNameEnable();
        // 全部启用的部门树
        List<OrganazationVo> departmentList = this.departmentService.getDepartmentTree();
        // 全部启用的岗位树
        Set<Integer> copyOfUserIds = Sets.newHashSet(userIdDTO.getIds());
        for (SystemUserEditOrViewVO systemUserEditOrViewVO : systemUserByIdVOs) {

            systemUserEditOrViewVO.setAllRoleIdNames(allRoleIdNames);
            systemUserEditOrViewVO.setDepartmentList(departmentList);
            List<OrganazationVo> positionList = this.positionService.getDeptPositionTree(systemUserEditOrViewVO.getDeptId().toString(), 0, 2,null);
            systemUserEditOrViewVO.setPositionList(positionList);
            copyOfUserIds.remove(systemUserEditOrViewVO.getId());
        }
        if (copyOfUserIds.size() > 0) {
            return Result.succeedWith(systemUserByIdVOs, CodeEnum.SUCCESS.getCode(), String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), StringUtils.join(copyOfUserIds, ",")));
        }

        return Result.succeed(systemUserByIdVOs);
    }

    @PostMapping("/saveRoleInfo")
    @ApiOperation(value = "授权保存操作")
    public Result<BatchOperateResultVO> saveRoleInfo(@RequestBody @Validated RoleInfoDTO roleInfoDto) {

        JacksonUtil.dumnToPrettyJsonInfo("授权保存操作: ", roleInfoDto);

        Set<Integer> roleIds = roleInfoDto.getRoleIds();
        if (CollectionUtils.isEmpty(roleIds) && roleInfoDto.getOpenSystem() == 1) {
            return Result.failed("请指定授予角色");
        }
        Set<Integer> userIds = roleInfoDto.getUserIds();
        checkNumberIds(userIds);
        Collection<SystemUserDO> systemUserDOs = this.systemUserService.listByIds(userIds);
        if (CollectionUtils.isEmpty(systemUserDOs)) {
            return Result.failed(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), StringUtils.join(userIds, ",")));
        }
        Set<Integer> copyOfUserIds = Sets.newHashSet(userIds);
        BatchOperateResultVO<String, String> batchOperateResultVO = new BatchOperateResultVO<>();
        String msgFormat = "员工id[%s],手机号[%s]";
        for (SystemUserDO systemUserDO : systemUserDOs) {
            copyOfUserIds.remove(systemUserDO.getId());
            try {
                boolean roleOk = systemUserService.saveRoleInfo(systemUserDO, roleInfoDto, this.getSystemUser());
                if (roleOk) {
                    batchOperateResultVO.getSuccessKeys().add(String.format(msgFormat, systemUserDO.getId(), systemUserDO.getPhoneNumber()) + " 离职成功");
                } else {
                    batchOperateResultVO.setAllSuccess(false);
                    batchOperateResultVO.getKeyToMsg().put(systemUserDO.getId().toString(), String.format(msgFormat, systemUserDO.getId(), systemUserDO.getPhoneNumber()) + " 离职失败，请重试");
                }
            } catch (Exception e) {
                batchOperateResultVO.setAllSuccess(false);
                batchOperateResultVO.getKeyToMsg().put(systemUserDO.getId().toString(), e.getMessage());
            }
        }
        // 有未查询用户的用户id
        if (copyOfUserIds.size() > 0) {
            String joinIds = StringUtils.join(copyOfUserIds, ",");
            batchOperateResultVO.getKeyToMsg().put(joinIds, String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), joinIds));
        }
        if (batchOperateResultVO.isAllSuccess()) {
            return Result.succeed();
        }
        return Result.failed(batchOperateResultVO, "未全部成功");
    }

    @PostMapping("/dimission")
    @ApiOperation(value = "离职、批量离职")
    public Result<BatchOperateResultVO> dimission(@RequestBody @Validated UserIdDTO userIdDTO) {

        JacksonUtil.dumnToPrettyJsonInfo("离职: ", userIdDTO);

        checkNumberIds(userIdDTO.getIds());
        Collection<SystemUserDO> systemUserDOs = this.systemUserService.listByIds(userIdDTO.getIds());
        if (CollectionUtils.isEmpty(systemUserDOs)) {
            return Result.failed(String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), StringUtils.join(userIdDTO.getIds(), ",")));
        }
        Set<Integer> copyOfUserIds = Sets.newHashSet(userIdDTO.getIds());
        BatchOperateResultVO<String, String> batchOperateResultVO = new BatchOperateResultVO<>();
        String msgFormat = "员工id[%s],手机号[%s]";
        for (SystemUserDO systemUserDO : systemUserDOs) {
            copyOfUserIds.remove(systemUserDO.getId());
            // 已是离职状态，不能重复离职
            if (!systemUserDO.isInService()) {
                continue;
            }
            try {
                boolean dimission = systemUserService.dimission(systemUserDO, this.getSystemUser());
                if (dimission) {
                    batchOperateResultVO.getSuccessKeys().add(String.format(msgFormat, systemUserDO.getId(), systemUserDO.getPhoneNumber()) + " 离职成功");
                } else {
                    batchOperateResultVO.setAllSuccess(false);
                    batchOperateResultVO.getKeyToMsg().put(systemUserDO.getId().toString(), String.format(msgFormat, systemUserDO.getId(), systemUserDO.getPhoneNumber()) + " 离职失败，请重试");
                }
            } catch (Exception e) {
                batchOperateResultVO.setAllSuccess(false);
                batchOperateResultVO.getKeyToMsg().put(systemUserDO.getId().toString(), e.getMessage());
            }
        }
        // 有未查询用户的用户id
        if (copyOfUserIds.size() > 0) {
            String joinIds = StringUtils.join(copyOfUserIds, ",");
            batchOperateResultVO.getKeyToMsg().put(joinIds, String.format(CodeEnum.SAAS_OPERA_MANAGER_20212001.getMsg(), joinIds));
        }
        if (batchOperateResultVO.isAllSuccess()) {
            return Result.succeed();
        }
        return Result.failed(batchOperateResultVO, "未全部成功");
    }

    @PostMapping("/listOnePage")
    @ApiOperation(value = "列表分页查询")
    public Result<PageResult<SystemUserListVO>> listOnePage(@RequestBody @Validated SystemUserSearchDTO systemUserSearchDTO) {

        setAuthParam(systemUserSearchDTO);
        JacksonUtil.dumnToPrettyJsonInfo("列表分页查询: ", systemUserSearchDTO);

        PageInfo<SystemUserListVO> pageInfo = new PageInfo<>(this.systemUserService.listOnePage(systemUserSearchDTO));
        PageResult<SystemUserListVO> pageResult = PageResult.getPageResult(pageInfo);
        ListHeaderUtil.setListHeaders(pageResult, SystemUserListVO.class);
        return Result.succeed(pageResult);
    }

    private void setAuthParam(@Validated @RequestBody SystemUserSearchDTO systemUserSearchDTO) {
        TokenUser tokenUser = this.getSystemUser();
        systemUserSearchDTO.setId(tokenUser.getId());
        SystemUserDO byId = this.systemUserService.getById(tokenUser.getId());
        if (null == byId) {
            throw new ServiceException(CodeEnum.ILLEGAL_ACCESS_TOKEN);
        }

        /** 获取当前用户的部门id与岗位id*/
        systemUserSearchDTO.setDeptId(byId.getDeptId());
        systemUserSearchDTO.setPositionId(byId.getPositionId());
    }


    @GetMapping("/downloadImportTemplate")
    @ApiOperation(value = "下载导出员工模板")
    public void downloadImportTemplate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = "员工导入模版.xlsx";
        String path = "/templates/";
        //获取模板
        ClassPathResource cpr = new ClassPathResource(path + fileName);
        //判断浏览器类型
        if (isMSBrowser(request)) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } else {
            //万能乱码问题解决
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + fileName);
        response.setHeader("progma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,no-store");
        response.setDateHeader("Expires", 0);

        ServletOutputStream out = response.getOutputStream();
        IOUtils.copy(cpr.getInputStream(), out);
        out.flush();
        out.close();
    }


    @PostMapping("/importExcel")
    @ApiOperation(value = "导入员工")
    public Result importExcel(@NotNull(message = "员工excel不可为空") @RequestParam(value = "excelFile") MultipartFile excelFile) {
        try {
            synchronized (SystemUserImportDTO.class) {
                SystemUserImportListener systemUserImportListener = new SystemUserImportListener(this.systemUserService, this.departmentService, this.positionService, this.systemRoleService, empUtil);
                ExcelUtil.readExcel(excelFile, this.getSystemUser(), SystemUserImportDTO.class, systemUserImportListener);
                List<String> errorList = systemUserImportListener.getErrorList();
                // 保存
                if (errorList.isEmpty()) {
                    systemUserService.saveList(systemUserImportListener.getToSaveList());
                } else {
                    return Result.failed(errorList, "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e.getCause();
            String msg = e.getMessage();
            if (null != cause) {
                msg = cause.getMessage();
            }
            log.info(msg);
            return Result.failed(msg);
        }
        return Result.succeed();
    }

    @Resource
    private AliyunOSSConfig aliyunOSSConfig;

    @PostMapping("/exportExcel")
    @ApiOperation(value = "导出员工")
    public Result exportExcel(@RequestBody SystemUserSearchDTO systemUserSearchDTO) {

        setAuthParam(systemUserSearchDTO);

        List<SysUserExportVO> list = this.systemUserService.getSysUserExportVOList(systemUserSearchDTO);
        if (CollectionUtils.isEmpty(list)) {
            return Result.succeed("未查询到员工信息");
        }
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(aliyunOSSConfig.getEndpoint(), aliyunOSSConfig.getAccessKeyId(),
                aliyunOSSConfig.getAccessKeySecret());
        try {
            String rootExportPath = ExcelUtil.getExportTmpPath();
            String fileName = "运营SaaS导出员工-" + DateUtil.nowDateTime2() + ExcelTypeEnum.XLSX.getValue();
            String filePath = rootExportPath + fileName;
            ExcelUtil.writeDataToExcelFile(filePath, list, "sheet1", ExcelTypeEnum.XLSX, SysUserExportVO.class);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = format.format(new Date());
            // 设置文件路径
            String objectName = aliyunOSSConfig.getFolderhost() + "/" +
                    aliyunOSSConfig.getFoldsecond() + "/" + dateStr + "/export/" +
                    fileName;

            PutObjectResult result = ossClient.putObject(aliyunOSSConfig.getBucketName(), objectName,
                    new FileInputStream(filePath));
            // 设置权限(公开读)
            ossClient.setBucketAcl(aliyunOSSConfig.getBucketName(), CannedAccessControlList.PublicRead);
            if (result == null) {
                log.error("OSS文件服务器上传失败");
                return Result.failed(CodeEnum.OSS_UPLOD_ERROR);
            }

            // 删除本地文件
            new File(filePath).delete();

            // 文件访问全url
            String fileFullUrl = "https://" + aliyunOSSConfig.getBucketName() + "." +
                    aliyunOSSConfig.getEndpoint() + "/" + objectName;
            log.info("fileFullUrl==" + fileFullUrl);
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("url", fileFullUrl);
            return Result.succeed(hashMap);
        } catch (Exception e) {
            log.info("员工导出异常", e);
            return Result.failed("员工导出异常");
        } finally {
            // 关闭OSSClient
            ossClient.shutdown();
        }
    }

    private static Set<String> IEBrowserSignals = Sets.newHashSet("MSIE", "Trident", "Edge");

    /**
     * 判断是否为IE浏览器
     *
     * @param request 请求信息
     * @return boolean
     */
    private boolean isMSBrowser(HttpServletRequest request) {

        return IEBrowserSignals.contains(request.getHeader("User-Agent"));
    }

}


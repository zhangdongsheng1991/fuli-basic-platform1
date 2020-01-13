package com.fuli.cloud.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * <pre>
 * Description: 导出员工VO
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/1
 */
@Data
public class SysUserExportVO extends BaseRowModel {

    /**
     * 员工姓名、手机号、身份证号码、邮箱、所属部门、所属岗位、SaaS平台权限、SaaS平台角色、在职状态、入职时间、创建时间、创建人、更新时间
     */
    // 员工id
    private Integer id;

    @ExcelProperty(index = 0, value = " 员工姓名")
    private String name;

    @ExcelProperty(index = 1, value = "手机号")
    private String phoneNumber;

    @ExcelProperty(index = 2, value = "身份证号码")
    private String certificateCard;

    @ExcelProperty(index = 5, value = "邮箱")
    private String email;

    @ExcelProperty(index = 6, value = "所属部门")
    private String deptName;

    @ExcelProperty(index = 7, value = "所属岗位")
    private String positionName;

//SaaS平台权限、SaaS平台角色、在职状态、入职时间、创建时间、创建人、更新时间

    // 是否开通saas权限 0-不开通，1-开通
    private Integer openSystem;
    @ExcelProperty(index = 8, value = "SaaS平台权限")
    private String openSystemStr;

    public String getOpenSystemStr() {
        if (this.openSystem == 0) {
            return "未开通";
        } else {
            return "已开通";
        }
    }

    @ExcelProperty(index = 9, value = "SaaS平台角色")
    private String saasRoleNamesStr = "";

    // 状态(1.enable,0. disable ,-1 deleted)
    private Integer status;
    @ExcelProperty(index = 10, value = "在职状态")
    private String statusStr;

    public String getStatusStr() {
        if (this.status == 0) {
            return "离职";
        } else if (this.status == 1) {
            return "在职";
        } else {
            return "删除";
        }
    }

    @ExcelProperty(index = 11, value = "入职时间")
    private String employmentDate;

    public String getEmploymentDate() {
        if (this.employmentDate != null && this.employmentDate.contains(" ")) {
            return this.employmentDate.split(" ")[0];
        }
        return employmentDate;
    }

    @ExcelProperty(index = 12, value = "创建时间")
    private Date createTime;

    @ExcelProperty(index = 13, value = "修改时间")
    private Date updateTime;

    @ExcelProperty(index = 14, value = "创建人")
    private String createOperator;
}

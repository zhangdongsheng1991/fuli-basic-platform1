package com.fuli.cloud.dto.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


/**
 * <pre>
 * Description: 导入员工Excel模型（专用，不要随便添加字段）
 * </pre>
 *
 * @author chenyi
 * @date 9:41 2019/8/1
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class SystemUserImportDTO extends BaseRowModel {

    @NotBlank(message = "姓名不能为空")
    @NotNull(message = "姓名不能为空")
    @Pattern(regexp = "^(?![·_\\-/]+$)[A-Za-z\\u4e00-\\u9fa5·_\\-/]{1,20}$"
            , message = "姓名只支持中文、英文、符号（·_-/），不可全部为符号，最长20个字符")
    @ExcelProperty(index = 0)
    private String name;

    @NotNull(message = "请输入正确格式的身份证号码")
    @NotBlank(message = "请输入正确格式的身份证号码")
//    @Length(min = 1, max = 18, message = "请输入正确格式的身份证号码")
    @ExcelProperty(index = 1)
    private String certificateCard;

    @NotNull(message = "请输入正确格式的手机号码")
    @NotBlank(message = "请输入正确格式的手机号码")
    @Length(min = 11, max = 11, message = "请输入正确格式的手机号码")
    @Pattern(regexp = "^1[0-9]{10}$", message = "请输入正确格式的手机号码")
    @ExcelProperty(index = 2)
    private String phoneNumber;

    @NotBlank(message = "部门不能为空")
    @NotNull(message = "部门不能为空")
    @Length(max = 20, message = "部门最大只能为{max}字符")
    @ExcelProperty(index = 3)
    private String depName;

    @NotBlank(message = "岗位不能为空")
    @NotNull(message = "岗位不能为空")
    @Length(max = 32, message = "岗位最大只能为{max}字符")
    @ExcelProperty(index = 4)
    private String position;

    @NotBlank(message = "入职时间不能为空")
    @NotNull(message = "入职时间不能为空")
    @Length(max = 32, message = "入职时间最大只能为{max}字符")
    @ExcelProperty(index = 5)
    private String employmentDate;

    @Length(max = 64, message = "SaaS平台角色最大只能为{max}字符")
    @ExcelProperty(index = 6)
    private String role;

}

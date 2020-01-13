package com.fuli.cloud.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.Set;

/**
 * <pre>
 * Description: 保存和新增DTO
 * </pre>
 *
 * @author chenyi
 * @date 2019/7/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserSaveDTO {

    @Min(value = 0, message = "员工id不能为负数")
    @ApiModelProperty("用户表 - 主键id（修改时需传入）")
    private Integer id;

    @NotBlank(message = "姓名不能为空")
    @NotNull(message = "姓名不能为空")
    @Pattern(regexp = "^(?![·_\\-/]+$)[A-Za-z\\u4e00-\\u9fa5·_\\-/]{1,20}$"
            , message = "姓名只支持中文、英文、符号（·_-/），不可全部为符号，最长20个字符")
    @ApiModelProperty(name = "name", value = "姓名", required = true)
    private String name;

    @NotBlank(message = "手机号码不能为空")
    @NotNull(message = "手机号码不能为空")
    @Pattern(regexp = "^1[0-9]{10}$", message = "手机号格式错误")
    @Length(min = 11, max = 11, message = "手机号必须为11位")
    @ApiModelProperty(name = "phoneNumber", value = "手机号码", required = true)
    private String phoneNumber;

    @NotBlank(message = "身份证号码不能为空")
    @NotNull(message = "身份证号码不能为空")
    @Length(max = 18, message = "身份证号码：不超过18个字符")
    @ApiModelProperty(name = "certificateCard", value = "身份证号码", required = true)
    private String certificateCard;

    @Email(message = "邮箱格式错误")
    @ApiModelProperty(name = "email", value = "邮箱")
    private String email;

    @NotNull(message = "部门id不能为空")
    @ApiModelProperty(value = "部门id", required = true)
    private Integer deptId;

    @NotNull(message = "岗位id不能为空")
    @ApiModelProperty(value = "岗位id", required = true)
    private Integer positionId;

    @NotNull(message = "是否开通运营权限标记必填")
    @Min(value = 0, message = "是否开通运营权限标记不合法")
    @Max(value = 1, message = "是否开通运营权限标记不合法")
    @ApiModelProperty(value = "是否开通运营权限 0-不开通，1-开通", required = true)
    private Integer openSystem;

    @NotNull(message = "入职时间不能为空")
    @ApiModelProperty(value = "入职时间", required = true, example = "2019-04-03 00:00:00")
    private Date employmentDate;

    @ApiModelProperty(value = "角色id集合")
    private Set<Integer> roleIds;

    @NotNull(message = "保存类型不能为空")
    @Min(value = 0, message = "保存类型不合法")
    @Max(value = 2, message = "保存类型不合法")
    @ApiModelProperty(value = "保存类型 0-首次入职，1-离职后重新入职,2-编辑保存", required = true)
    private Integer saveType;

    private boolean batchImport;

}

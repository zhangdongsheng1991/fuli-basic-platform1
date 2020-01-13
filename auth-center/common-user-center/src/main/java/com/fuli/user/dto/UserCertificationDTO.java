package com.fuli.user.dto;

import com.fuli.user.utils.RegexUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Description:    用户实名认证请求类
 * @Author:         WFZ
 * @CreateDate:     2019/8/7 16:07
 * @Version:        1.0
*/
@Data
public class UserCertificationDTO {

    @ApiModelProperty(value = "用户id",required = true)
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @ApiModelProperty(value = "企业id")
    private Long companyId;

    @ApiModelProperty(value = "员工id")
    private Long employeeId;

    @ApiModelProperty(value = "身份证号码",required = true)
    @NotBlank(message = "身份证号码不能为空")
    private String idNum;

    @ApiModelProperty(value = "手机号",required = true)
    @NotBlank(message = "手机号码不能为空")
    @Pattern(regexp = RegexUtil.PHONE,message = "手机号码格式有误")
    private String phoneNumber;

    @ApiModelProperty(value = "姓名",required = true)
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "身份证正面照片",required = true)
    @NotBlank(message = "身份证正面照片不能为空")
    private String imgPositive;

    @ApiModelProperty(value = "身份证反面照片" ,required = true)
    @NotBlank(message = "身份证正面照片不能为空")
    private String imgReverse;
}
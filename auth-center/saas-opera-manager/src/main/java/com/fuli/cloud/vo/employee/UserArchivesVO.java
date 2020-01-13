package com.fuli.cloud.vo.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description: 用户档案信息
 * @Author: WFZ
 * @CreateDate: 2019/8/1 14:57
 * @Version: 1.0
 */
@ApiModel(description = "用户档案信息")
@Data
@ToString
public class UserArchivesVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "部门")
    private String departmentName;

    @ApiModelProperty(value = "岗位")
    private String positionName;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "入职日期")
    private String employmentDate;


}

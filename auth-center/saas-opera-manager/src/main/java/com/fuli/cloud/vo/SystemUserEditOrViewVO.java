package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author chenyi
 * @date 2019/7/30
 */
@Data
public class SystemUserEditOrViewVO implements Serializable {

    @ApiModelProperty(name = "id", value = "员工id")
    private Integer id;

    @ApiModelProperty(name = "status", value = "员工状态 离职：0，在职：1")
    private Integer status;

    @ApiModelProperty(name = "name", value = "员工姓名")
    private String name;

    @ApiModelProperty(name = "username", value = "系统账号")
    private String username;

    @ApiModelProperty(name = "phoneNumber", value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(name = "certificateCard", value = "身份证号")
    private String certificateCard;

    @ApiModelProperty(name = "email", value = "邮箱")
    private String email;

    @ApiModelProperty(name = "deptId", value = "部门id")
    private Integer deptId;

    @ApiModelProperty(name = "deptName", value = "部门")
    private String deptName;

    @ApiModelProperty(name = "positionId", value = "岗位id")
    private Integer positionId;

    @ApiModelProperty(name = "position", value = "岗位")
    private String positionName;

    @ApiModelProperty(name = "employmentDate", value = "入职时间")
    private Date employmentDate;

    @ApiModelProperty(name = "openSystem", value = "是否开通运营权限 0-不开通，1-开通")
    private Integer openSystem;

    @ApiModelProperty(name = "roleIdNames", value = "用户角色信息")
    private List<RoleIdNameVo> roleIdNames;

    @ApiModelProperty(name = "allRoleIdNames", value = "全部启用角色信息")
    private List<RoleIdNameVo> allRoleIdNames;

    @ApiModelProperty(name = "departmentList", value = "全部启用部门结构树")
    private List<OrganazationVo> departmentList;

    @ApiModelProperty(name = "positionList", value = "员工所在部门的岗位结构树")
    private List<OrganazationVo> positionList;

}

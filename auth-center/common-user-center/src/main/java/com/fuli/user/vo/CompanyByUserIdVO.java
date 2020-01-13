package com.fuli.user.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:    根据用户id获取企业列表（开通SaaS门户权限）响应类
 * @Author:         WFZ
 * @CreateDate:     2019/7/25 10:46
 * @Version:        1.0
*/
@ApiModel(description = "根据用户id获取企业列表（开通SaaS门户权限）")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CompanyByUserIdVO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "企业在集团(中台)唯一id")
    private String companyOpenId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "员工姓名")
    private String name;

    @ApiModelProperty(value = "是否为默认展示企业0:否 1：是（APP）")
    private String isShow;

    @ApiModelProperty(value = "是否为项目人员 0.否,1.是")
    private String projectFlag;

    @ApiModelProperty(value = "员工状态 1 正常，其他不正常")
    private String employeeStatus;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    @ApiModelProperty(value = "社会信用码")
    private String companyCreditCode;

    @ApiModelProperty(value = "部门名称（项目名称）")
    private String positionName;

    @ApiModelProperty(value = "岗位名称")
    private String deptName;

    @ApiModelProperty(value = "默认展示企业员工id",hidden = true)
    private Long dId;

    /*@ApiModelProperty(value = "是否开通SaaS权限0:否 1：是")
    private String openSaaS;*/

}

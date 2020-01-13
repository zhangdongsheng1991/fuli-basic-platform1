package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增运营用户关联企业权限
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class ListUserGrantByCompanyIdVO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "用户姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "岗位名称")
    private String positionName;

}

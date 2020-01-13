package com.fuli.cloud.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 用户表
 * @author WFZ 2019-07-29
 */
@Data
@ApiModel("用户表")
public class SystemUserDTO {

    @ApiModelProperty("用户表 - 主键id")
    private Integer id;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("系统账号")
    private String username;

    @ApiModelProperty("用户手机号")
    private String phoneNumber;

    @ApiModelProperty("身份证号码")
    private String certificateCard;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("状态(1.在职，0.离职，-1.删除)")
    private Integer status;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("岗位id")
    private Integer positionId;

    @ApiModelProperty("性别 0-默认值未知，1-男，2-女，3-保密")
    private Integer gender;

    @ApiModelProperty("是否开通运营权限 0-不开通，1-开通")
    private Integer openSystem;

    @ApiModelProperty("创建时间/入职时间")
    private Date createTime;

    @ApiModelProperty("创建人")
    private String createOperator;

    @ApiModelProperty("离职时间")
    private Date dimissionDate;

    @ApiModelProperty("操作人id")
    private Integer opId;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("操作人用户账号")
    private String operationAccount;

    @ApiModelProperty("备注")
    private String remark;

    public SystemUserDTO() {
    }

}

package com.fuli.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 员工表
 * @author WFZ 2019-09-29
 */
@Data
@ApiModel("员工表")
@TableName("employee")
public class EmployeeDO {

    @ApiModelProperty("员工id")
    @TableId(value="id",type= IdType.ID_WORKER)
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    @ApiModelProperty("中台用户唯一id")
    private String openId;

    @ApiModelProperty("企业在业务系统id")
    private Long companyId;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("员工手机号")
    private String phoneNumber;

    @ApiModelProperty("证件类型: 1.身份证，2护照号， 3.")
    private Integer certificateType;

    @ApiModelProperty("证件号码")
    private String certificateCard;

    @ApiModelProperty("状态(1.enable，0. disable ，-1 deleted)")
    private Integer status;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("部门id")
    private Integer deptId;

    @ApiModelProperty("岗位id")
    private Integer positionId;

    @ApiModelProperty("性别 0-默认值未知，1-男，2-女，3-保密")
    private Integer gender;

    @ApiModelProperty("员工来源（fljr.本系统）")
    private String userFrom;

    @ApiModelProperty("是否开通saas权限 0-不开通，1-开通")
    private Integer openSaas;

    @ApiModelProperty("入职时间")
    private Date employmentDate;

    @ApiModelProperty("离职时间")
    private Date dimissionDate;

    @ApiModelProperty("是否默认展示(0否，1展示)")
    private Integer isShow;

    @ApiModelProperty("是否为项目 0.否，1.是")
    private Integer projectFlag;

    @ApiModelProperty("创建人id")
    private Long createOpId;

    @ApiModelProperty("创建人")
    private String createOperator;

    @ApiModelProperty("操作人id")
    private Long opId;

    @ApiModelProperty("操作人")
    private String operator;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date updateTime;

    @ApiModelProperty("备注")
    private String remark;

    public EmployeeDO() { }

}
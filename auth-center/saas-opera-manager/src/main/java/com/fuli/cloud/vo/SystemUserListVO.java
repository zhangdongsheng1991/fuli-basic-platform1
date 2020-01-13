package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.commons.annotation.ListHeader;
import com.fuli.cloud.constant.DateConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;


/**
 * <pre>
 * Description: 员工列表VO实体
 * </pre>
 *
 * @author chenyi
 * @date 16:23 2019/7/31
 **/
@Data
@ApiModel("员工列表VO实体")
@FieldNameConstants
public class SystemUserListVO implements Serializable {

    @ApiModelProperty(name = "id", value = "员工id")
    private Integer id;

    @ListHeader(headerName = "姓名")
    @ApiModelProperty("员工姓名")
    private String name;

    @ListHeader(headerName = "手机号")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ListHeader(headerName = "身份证号码")
    @ApiModelProperty("身份证号码")
    private String certificateCard;

    @ListHeader(headerName = "所属部门")
    @ApiModelProperty("所属部门")
    private String departmentName;

    @ListHeader(headerName = "所属岗位")
    @ApiModelProperty("所属岗位")
    private String positionName;

    @ListHeader(headerName = "SaaS平台权限")
    @ApiModelProperty("是否开通运营权限 0-不开通，1-开通")
    private String openSystem;

    @ApiModelProperty("SaaS平台角色")
    private String saasRoleNamesStr = "";

    @ListHeader(headerName = "在职状态")
    @ApiModelProperty("在职状态 状态(1.在职,0.离职)")
    private String status;

    @ListHeader(headerName = "创建时间")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME, timezone = DateConstant.TIME_ZONE)
    private Date createTime;

    @ListHeader(headerName = "创建人")
    @ApiModelProperty("创建人")
    private String createOperator;

    @ListHeader(headerName = "更新时间")
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME, timezone = DateConstant.TIME_ZONE)
    private Date updateTime;
}

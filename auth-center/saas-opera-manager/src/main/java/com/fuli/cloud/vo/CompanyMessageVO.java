package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.constant.DateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class CompanyMessageVO implements Serializable {

    private static final long serialVersionUID = -3052255435315777630L;

    @ApiModelProperty(value = "留言ID")
    private Long id;

    @ApiModelProperty(value = "留言企业ID")
    private Long companyId;

    @ApiModelProperty(value = "留言企业名称")
    private String companyName;

    @ApiModelProperty(value = "留言人姓名")
    private String realName;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "电子邮箱")
    private String email;

    @ApiModelProperty(value = "留言内容")
    private String content;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "处理状态（结果） 0：待处理；1：有效处理；2：无效处理")
    private Short state;

    @ApiModelProperty(value = "留言时间")
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date createTime;

    @ApiModelProperty(value = "处理时间")
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date dealTime;

//    @ApiModelProperty(value = "操作人")
//    private Integer dealUser;

}

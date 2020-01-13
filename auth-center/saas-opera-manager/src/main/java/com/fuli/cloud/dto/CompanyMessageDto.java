package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class CompanyMessageDto implements Serializable {

    private static final long serialVersionUID = 3251927912339234034L;

    @ApiModelProperty("留言ID")
    private Long id;

    @ApiModelProperty("留言企业ID")
    private Long companyId;

    @ApiModelProperty("留言企业名称")
    private String companyName;

    @ApiModelProperty("留言人姓名")
    private String realName;

    @ApiModelProperty("联系电话")
    private String telephone;

    @ApiModelProperty("处理结果 0：待反馈；1：有效留言；2：无效留言")
    private Short state;

    @ApiModelProperty("处理状态 0:待处理；1：已处理")
    private Short dealState;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "页码", example = "1")
    @Range(min = 1, max = 1000, message = "页码参数必须在{min}-{max}之间")
    protected Integer pageNum = 1;

    @ApiModelProperty(value = "每页数据条数", example = "6")
    @Range(min = 1, max = 100, message = "页大小参数必须在{min}-{max}之间")
    protected Integer pageSize = 10;

    @ApiModelProperty(value = "开始时间", example = "2019-05-25 00:00:00")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-05-25 23:59:59")
    private String endTime;



}

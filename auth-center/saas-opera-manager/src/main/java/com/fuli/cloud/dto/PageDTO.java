package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 分页dto
 * @author pcg
 * @date 2019-6-21 12:19:17
 */
@Data
public class PageDTO {
    
    @ApiModelProperty(value = "页码", example = "1")
    @Range(min = 1, max = 1000, message = "页码参数必须在{min}-{max}之间")
    protected Integer pageNum = 1;

    @ApiModelProperty(value = "每页数据条数", example = "6")
	@Range(min = 1, max = 100, message = "页大小参数必须在{min}-{max}之间")
    protected Integer pageSize = 10;

    @ApiModelProperty(value = "开始时间", example = "2019-05-25 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty(value = "结束时间", example = "2019-05-25 23:59:59")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}

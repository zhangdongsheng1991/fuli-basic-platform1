package com.fuli.cloud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.commons.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @author chenyi
 * @date 2019/8/2
 */
@Data
public class QueryAnnouncementUserListDTO extends PageDTO {

    @Min(value = 0, message = "是否已读标记参数错误 ")
    @Max(value = 1, message = "是否已读标记参数错误 ")
    @ApiModelProperty("是否已读标记 0:未读，1:已读")
    private Integer readFlag;

    @ApiModelProperty(value = "创建时间", example = "2019-05-25 00:00:00")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(hidden = true)
    private Integer userId;

    @ApiModelProperty(hidden = true)
    private Date startCreateTime;

    public Date getStartCreateTime() {
        if (createTime == null) {
            return null;
        }
        return DateUtil.parseDate(createTime);
    }

    @ApiModelProperty(hidden = true)
    private Date endCreateTime;

    public Date getEndCreateTime() {
        if (createTime == null) {
            return null;
        }
        return DateUtil.decreaseOneSecond(DateUtil.incrementOneDay(DateUtil.parseDate(createTime)));
    }


    @ApiModelProperty(hidden = true)
    private Date startTime;

    @ApiModelProperty(hidden = true)
    private Date endTime;
}

package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author chenyi
 * @date 2019/8/2
 */
@Data
public class SystemAnnouncementVO {

    @ApiModelProperty("公告id")
    private Integer announcementId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("是否已读 0:未读，1:已读")
    private Integer readFlag;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("公告内容")
    private String messageContent;

    @ApiModelProperty("消息链接")
    private String messageLink;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;
}

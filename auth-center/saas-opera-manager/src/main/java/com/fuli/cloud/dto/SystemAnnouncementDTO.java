package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 运营saas系统公告
 *
 * @author chenyi 2019-08-02
 */
@Data
@ApiModel("运营saas系统公告DTO")
public class SystemAnnouncementDTO {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String messageContent;

    @ApiModelProperty("消息链接")
    private String messageLink;

}
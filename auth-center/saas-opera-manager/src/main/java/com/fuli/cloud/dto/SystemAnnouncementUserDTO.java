package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运营saas系统公告用户关联表
 *
 * @author chenyi 2019-08-02
 */
@Data
@ApiModel("运营saas系统公告用户关联表DTO")
public class SystemAnnouncementUserDTO {

    @ApiModelProperty("公告id")
    private Integer announcementId;

    @ApiModelProperty("saas用户id")
    private Integer userId;

    @ApiModelProperty("group_sent_message_id")
    private Integer groupSentMessageId;

    @ApiModelProperty("是否已读 0:未读，1:已读")
    private Integer readFlag;

    @ApiModelProperty("逻辑删除标记")
    private Integer deleteFlag;


}
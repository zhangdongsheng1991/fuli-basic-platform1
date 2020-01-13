package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description:    友盟推送
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 17:19
 * @Version:        1.0
*/
@Data
public class UserMsgDTO {

    @ApiModelProperty(value = "用户id")
    private Long id;
    @ApiModelProperty(value = "用户id")
    private Long userId;
    @ApiModelProperty(value = "是否启用消息推送 0：否；1：是")
    private Integer enableFlag;
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;
}

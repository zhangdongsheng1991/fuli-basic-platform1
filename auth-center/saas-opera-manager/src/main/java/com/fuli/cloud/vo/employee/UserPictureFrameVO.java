package com.fuli.cloud.vo.employee;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Description: 用户头像框信息
 * @Author: WFZ
 * @CreateDate: 2019/8/1 14:57
 * @Version: 1.0
 */
@ApiModel(description = "用户档案信息")
@Data
@ToString
public class UserPictureFrameVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "角色")
    private String roleName;

    @ApiModelProperty(value = "悬浮球开启状态，1-开启中 ， 2-未开启")
    private String openState;


}

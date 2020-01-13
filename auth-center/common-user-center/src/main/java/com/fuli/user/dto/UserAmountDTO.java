package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:    java类作用描述
 * @Author:
 * @CreateDate:     2019/8/7 15:58
 * @Version:        1.0
*/
@Data
public class UserAmountDTO {

    @ApiModelProperty(name = "userId",value = "用户id")
    private Long userId;

    @ApiModelProperty(name = "homeAmountFlag",value = "首页金额是否显示标识 0：不显示；1：显示")
    private Integer homeAmountFlag;
}

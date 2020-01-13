package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author chenyi
 * @date 2019/12/4
 */
@Data
public class UpdateEwalletInfoDTO {

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(name = "userId", value = "用户id", required = true)
    private Long userId;

    @ApiModelProperty(name = "haoYiLian", value = "是否开通数科好易联电子钱包 0:未开通 1:已开通")
    private Integer haoYiLian;

    @ApiModelProperty(name = "huaXiaPds", value = "是否开通数科华夏pds电子钱包 0:未开通 1:已开通")
    private Integer huaXiaPds;
}

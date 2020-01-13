package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@ApiModel
public class BatchUpdateReadDTO {

    @Size(min = 1, max = 5000, message = "系统消息Id集合不能为空，且一次批量操作最多{max}条")
    @NotNull(message = "系统消息id不能为空")
    @ApiModelProperty(value = "系统消息Id集合", required = true)
    private Set<Long> ids;
}

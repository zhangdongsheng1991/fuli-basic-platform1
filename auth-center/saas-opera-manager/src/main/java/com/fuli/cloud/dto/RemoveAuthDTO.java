package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * @author chenyi
 * @date 2019/12/4
 */
@Data
public class RemoveAuthDTO {

    @NotNull(message = "记录id集合不能为空")
    @NotEmpty(message = "记录id集合不能为空")
    @ApiModelProperty("记录id集合")
    @Size(min = 1, max = 100, message = "最多一次批量移除授权{max}条")
    private Set<Long> ids;
}

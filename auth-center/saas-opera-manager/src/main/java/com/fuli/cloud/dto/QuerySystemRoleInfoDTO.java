package com.fuli.cloud.dto;

import com.fuli.cloud.constant.ValidateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * @author xq
 */
@Data
public class QuerySystemRoleInfoDTO {
    @ApiModelProperty(value = "角色id", example = "1", required = true)
    @NotNull(message = ValidateConstant.ROLE_ID_NOT_NULL_MSG)
    private Integer id;

    @ApiModelProperty(value = "是否只显示选中的菜单 1 是 0 否", example = "1")
    @Range(min = 0, max = 1, message = ValidateConstant.ONLY_CHECKED_MSG)
    private Integer onlyChecked;

}

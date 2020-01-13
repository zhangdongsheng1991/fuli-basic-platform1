package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 岗位实体类
 * @author yhm
 * @date 2019/06/25
 */
@Data
@FieldNameConstants
public class PositionUpdateDto extends PositionAddDto implements Serializable {

    private static final long serialVersionUID = 2246069696021717539L;

    @ApiModelProperty("岗位ID")
    @NotNull(message = "岗位ID不能为空")
    private Integer id;


}
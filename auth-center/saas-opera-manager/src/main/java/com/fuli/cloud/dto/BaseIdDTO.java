package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 只有单个id入参类
 * @author yhm
 * @date 2019/07/31
 */
@Data
public class BaseIdDTO implements Serializable {

    @ApiModelProperty(value="主键ID",required = true)
    @NotBlank(message = "id不能为空")
    private String id;
}

package com.fuli.cloud.dto;

import com.fuli.cloud.constant.ValidateConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

/**
 * @author xq
 * 添加角色封装
 */
@Data
@Api(tags="唯一性校验DTO")
public class CheckUniquenessDTO {

    @ApiModelProperty(value = "名称 （部门名称，岗位名称，角色名称）")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "更新时必传")
    private String id;

    @ApiModelProperty(value = "类别；1-部门，2-岗位，3-角色", example = "1")
    @NotBlank(message = "类别不能为空")
    private String type;
}

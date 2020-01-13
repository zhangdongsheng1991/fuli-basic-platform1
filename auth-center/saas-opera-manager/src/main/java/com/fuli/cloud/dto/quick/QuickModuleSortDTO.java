package com.fuli.cloud.dto.quick;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:    快捷功能排序请求类
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class QuickModuleSortDTO implements Serializable {

    @ApiModelProperty(value = "主键",required = true)
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty(value = "排序值",required = true)
    @NotBlank(message = "排序值不能为空")
    private String sort;

}

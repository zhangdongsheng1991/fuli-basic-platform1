package com.fuli.cloud.dto.quick;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:    快捷功能批量插入请求类
 * @Author:         WFZ
 * @CreateDate:     2019/5/22 14:19
 * @Version:        1.0
*/
@Data
public class BatchInsertDTO implements Serializable {

    @ApiModelProperty(value = "模块id",required = true)
    @NotBlank(message = "模块id不能为空")
    private String moduleId;

    @ApiModelProperty(value = "排序值", required = true)
    @NotBlank(message = "排序值不能为空")
    private String sort;

}

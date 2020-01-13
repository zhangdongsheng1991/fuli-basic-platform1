package com.fuli.cloud.dto.homepage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:    首页服务管理
 * @Author:         WFZ
 * @CreateDate:     2019/6/26 17:39
 * @Version:        1.0
*/
@Data
public class HomepageUserDTO implements Serializable {

    private static final long serialVersionUID = 223424201423358886L;

    @ApiModelProperty(value = "首页模块id",required = true)
    @NotBlank(message = "模块id不能为空")
    private String homepageModuleId;

    @ApiModelProperty(value = "区分列；1-左，2-中，3-右",required = true)
    @NotBlank(message = "列类别不能为空")
    private String type;

    @ApiModelProperty(value = "排序值",required = true)
    private String sort;

}

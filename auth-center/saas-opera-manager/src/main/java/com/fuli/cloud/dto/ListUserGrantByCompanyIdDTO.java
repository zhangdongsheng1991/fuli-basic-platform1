package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 根据企业id获取已添加的管理人员列表
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class ListUserGrantByCompanyIdDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "企业id",example = "1",required = true)
    @NotNull(message = "企业id不能为空")
    private Long companyId;

    @ApiModelProperty(value = "业务线id",example = "10",required = true)
    @NotNull(message = "业务线id不能为空")
    private Integer moduleId;

}

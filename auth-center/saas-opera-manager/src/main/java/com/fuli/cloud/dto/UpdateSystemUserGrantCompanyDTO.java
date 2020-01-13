package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

/**
 * 新增运营用户关联企业权限
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class UpdateSystemUserGrantCompanyDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "业务线id",example = "10",required = true)
    @NotNull(message = "业务线id不能为空")
    private Integer moduleId;

    @ApiModelProperty(value="业务线名称",example = "薪发放",required = true)
    @NotNull(message = "业务线名称不能为空")
    private String moduleName;

    @ApiModelProperty(value = "企业id",example = "1",required = true)
    @NotNull(message = "企业id不能为空")
    private Long companyId;

    @ApiModelProperty(value="企业名称",example = "安徽。。",required = true)
    @NotNull(message = "企业名称不能为空")
    private String companyName;

    @NotEmpty(message = "请选择管理人")
    @ApiModelProperty(value = "管理人id集合",required = true)
    private Set<Integer> userIds;


}

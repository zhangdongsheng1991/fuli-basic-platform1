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
public class AddSystemUserGrantCompanyDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @NotEmpty(message = "请选择业务线")
    @ApiModelProperty(value = "业务线id集合",required = true)
    private Set<ModuleMapDTO> moduleIds;

    @NotEmpty(message = "请选择企业")
    @ApiModelProperty(value = "企业id集合",required = true)
    @Size(min = 1,max = 50,message = "一次授权最多选择50家企业")
    private Set<CompanyMapDTO> companyIds;

    @NotEmpty(message = "请选择管理人")
    @ApiModelProperty(value = "管理人id集合",required = true)
    @Size(min = 1,max = 50,message = "一次授权最多选择50个管理人")
    private Set<Integer> userIds;


}

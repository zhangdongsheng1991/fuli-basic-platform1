package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业id与名称对应
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class CompanyMapDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "企业id",example = "1",required = true)
    private Long companyId;

    @ApiModelProperty(value="企业名称",example = "安徽。。",required = true)
    private String companyName;


}

package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 新增运营用户关联企业权限
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class ListByUserIdGrantCompanyVO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value="企业id")
    private String companyId;

    @ApiModelProperty(value="企业名称")
    private String companyName;

    @ApiModelProperty(value="统一社会信用代码")
    private String businessLicenseCode;

}

package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  安徽赋力大数据处理服务有限公司
* @Description: (根据companyopenid、useropenid、payPassword校验密码入参实体)
* @author fengjing
* @date 2019/5/28 11:04
* @version V1.0
*/
@Data
public class CompanyUserVO {

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Long userid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户帐号")
    private String username;

    /**
     * 企业id
     */
    @ApiModelProperty(value = "企业id")
    private Long companyid;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String companyname;

    /**
     * 员工id
     */
    @ApiModelProperty(value = "员工id")
    private Long empid;
}

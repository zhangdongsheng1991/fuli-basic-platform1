package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 运营用户关联企业权限响应类
 * @author yhm
 * @date 2019/12/4
 */
@Data
public class SystemUserGrantCompanyVo implements Serializable {

    private static final long serialVersionUID = 1379772813499682974L;

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "模块(业务线)id")
    private Integer moduleId;

    @ApiModelProperty(value = "模块（业务线）名称")
    private String moduleName;

    @ApiModelProperty(value = "企业ID")
    private String companyId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "管理员ID")
    private Integer userId;

    @ApiModelProperty(value = "管理人姓名")
    private String name;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "操作人账号")
    private String operationAccount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}

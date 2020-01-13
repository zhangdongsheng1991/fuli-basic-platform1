package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description:    生成token需要的参数
 * @Author:         WFZ
 * @CreateDate:     2019/7/29 15:08
 * @Version:        1.0
*/
@Data
public class JwtDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "系统帐号")
    private String userAccount;

    @ApiModelProperty(value = "当前企业id")
    private String currentCompanyId;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    @ApiModelProperty(value = "客户端id")
    private String clientId;

    @ApiModelProperty(value = "社会企业信用代码")
    private String companyCreditCode;

}

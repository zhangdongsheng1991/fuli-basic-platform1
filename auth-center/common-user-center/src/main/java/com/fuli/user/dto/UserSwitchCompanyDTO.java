package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description: TODO
 * @Author: WS
 * @CreateDate: 2019/4/28 12:20
 * @Version: 1.0
 */
@Data
public class UserSwitchCompanyDTO implements Serializable {
    /**
     * 当前企业ID
     */
    @ApiModelProperty(value = "切换的企业ID",required = true)
    @NotBlank(message = "企业id不能为空")
    private String currentCompanyId;
    /**
     * 应用id
     */
    @ApiModelProperty(value = "客户端id",required = true)
    @NotBlank(message = "客户端id不能为空")
    private String client_id;
    /**
     * 用户帐号
     */
    @ApiModelProperty(value = "用户帐号或者手机号码",required = true)
    private String userAccount;

    /**
     * 原token
     */
    @ApiModelProperty(value = "原token")
    private String primaryToken;

    @ApiModelProperty(value = "员工id")
    private String employeeId;
}

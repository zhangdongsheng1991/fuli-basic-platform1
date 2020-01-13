package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @Description:    APP用户封装的登录信息 -- 请求头中
 * @Author:         WFZ
 * @CreateDate:     2019/8/8 10:42
 * @Version:        1.0
*/
@Data
public class TokenUserVO implements Serializable {

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "当前企业id")
    private Long currentCompanyId;

    @ApiModelProperty(value = "员工id")
    private Long employeeId;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户姓名")
    private String realName;

    @ApiModelProperty(value = "用户账户")
    private String userAccount;

    @ApiModelProperty(value = "客户端id")
    private String clientId;

    @ApiModelProperty(value = "社会企业信用代码")
    private String companyCreditCode;

    public String getUserAccount() {
        return StringUtils.isEmpty(userAccount) ? " " : userAccount;
    }

}

package com.fuli.cloud.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 重装密码 / 修改密码 请求DTO
 * @author WFZ 2019-07-29
 */
@Data
@ApiModel("重置密码 / 修改密码")
public class UserChangePasswordDTO {

    @ApiModelProperty(value = "用户帐号--忘记密码/修改初始密码必填，修改密码不需要" , required = true)
    private String mobile;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "原密码--修改密码/修改初始密码必填，忘记密码不需要")
    @NotBlank(message = "原密码不能为空")
    private String oldPwd;

    @ApiModelProperty(value = "确定密码" , required = true)
    @NotBlank(message = "确定密码不能为空")
    private String confirmPwd;

    public UserChangePasswordDTO() {
    }

}

package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Description:    java类作用描述
 * @Author:
 * @CreateDate:     2019/9/5 11:39
 * @Version:        1.0
*/
@Data
public class ResetPasswordDTO {

    @ApiModelProperty(value = "用户id" , required = true)
    @NotBlank(message = "用户id不能为空")
    private String userId;

    @ApiModelProperty(value = "用户密码")
    private String password;
}

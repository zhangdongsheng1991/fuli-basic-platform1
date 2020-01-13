package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description:    订阅中台用户修改
 * @Author:
 * @CreateDate:     2019/8/16 18:55
 * @Version:        1.0
*/
@Data
public class PlatformUpdateDTO {

  @ApiModelProperty(value = "用户在第三方业务系统的唯一ID")
  @NotNull(message = "用户id不能为空")
  private Long thirdUserId;

  @ApiModelProperty(value = "手机号")
  private String phoneNumber;

  @ApiModelProperty(value = "状态")
  private Integer status;

  @ApiModelProperty(value = "邮箱")
  private String email;
}

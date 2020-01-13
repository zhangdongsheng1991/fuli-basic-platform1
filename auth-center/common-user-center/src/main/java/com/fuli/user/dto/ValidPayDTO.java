package com.fuli.user.dto;

import com.fuli.user.utils.RegexUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Description:    支付密码校验请求类
 * @Author:         WFZ
 * @CreateDate:     2019/8/2 16:25
 * @Version:        1.0
*/
@Data
public class ValidPayDTO implements Serializable {

  @ApiModelProperty(value="用户id",required = true)
  @NotNull( message = "用户id不能为空")
  private Long userId;

  @ApiModelProperty(value="支付密码",required = true)
  @NotBlank(message = "支付密码不能为空")
  private String payPwd;
}

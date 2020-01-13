package com.fuli.user.dto;

import com.fuli.user.utils.RegexUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Description:    更换手机号请求类
 * @Author:         WFZ
 * @CreateDate:     2019/8/2 16:25
 * @Version:        1.0
*/
@Data
public class ChangePhoneDTO implements Serializable {

  @ApiModelProperty(value="手机号",required = true)
  @NotBlank(message = "手机号码不能为空")
  @Pattern(regexp = RegexUtil.PHONE,message = "手机号码格式有误")
  private String newPhone;

  @ApiModelProperty(value="短信验证码",required = true)
  @NotBlank(message = "验证码不能为空")
  private String verifyCode;
}

package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description:    接收token
 * @Author:         WFZ
 * @CreateDate:     2019/8/12 11:35
 * @Version:        1.0
*/
@Data
public class JwtVO {

  @ApiModelProperty(value = "令牌")
  private String access_token;

  @ApiModelProperty(value = "头部类型")
  private String token_type;

  @ApiModelProperty(value = "刷新令牌")
  private String refresh_token;

  @ApiModelProperty(value = "有效时间")
  private int expires_in;

  @ApiModelProperty(value = "用户手机号")
  private String phone;

  @ApiModelProperty(value = "appUser用户唯一标识主键")
  private String id;

  @ApiModelProperty(value = "真实姓名")
  private String realName;

  @ApiModelProperty(value = "帐号")
  private String userAccount;

  @ApiModelProperty(value = "当前企业id")
  private String currentCompanyId;

  @ApiModelProperty(value = "员工id")
  private String employeeId;

  @ApiModelProperty(value = "企业唯一信用代码")
  private String companyCreditCode;
}

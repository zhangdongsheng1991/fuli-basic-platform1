package com.fuli.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description:    返回Oauth
 * @Author:         FZ
 * @CreateDate:     2019/4/15 16:30
 * @Version:        1.0
 */
@Data
public class AppUserVo  implements Serializable {

    private static final long serialVersionUID = -5886012896705137070L;

    @ApiModelProperty(value = "用户id")
    private String id;

    @ApiModelProperty(value = "用户手机号")
    private String phone;

    @ApiModelProperty(value = "用户帐号")
    private String username;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "企业id")
    private String companyId;

    @ApiModelProperty(value = "用户来源，中文首字母")
    private String userFrom;

    @ApiModelProperty(value = "员工id")
    private String employeeId;

    @ApiModelProperty(value = "企业社会信用码")
    private String companyCreditCode;

    @ApiModelProperty(value = "是否为项目 0.否,1.是")
    private String projectFlag;

    @ApiModelProperty(value = "员工状态 1 正常，其他不正常")
    private String employeeStatus;

    /*@ApiModelProperty(value = "是否开通SaaS权限0:否 1：是")
    private String openSaaS;*/

}

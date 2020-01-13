package com.fuli.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    中台同步数据请求类
 * @Author:         WFZ
 * @CreateDate:     2019/8/12 14:55
 * @Version:        1.0
*/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppUserDTO implements Serializable {

    @ApiModelProperty(value="手机号")
    private String phoneNumber;

    @ApiModelProperty(value="用户在集团平台系统的唯一Id")
    private String openId;

    @ApiModelProperty(value="真实姓名")
    private String username;

    @ApiModelProperty(value=" 邮箱")
    private String email;

    @ApiModelProperty(value=" 性别：0-默认值未知，1-男，2-女，3-保密")
    private Integer gender;

    @ApiModelProperty(value=" 用户状态（中台状态）：1.正常,0.无效 ,-1.删除")
    private Integer status;

    @ApiModelProperty(value="证件类型1.身份证,2护照号")
    private Integer certificateType;

    @ApiModelProperty(value="证件号码")
    private String certificateCard;

    @ApiModelProperty(value="新用户来源 ， 本地：FLJR")
    private String userFrom;

    @ApiModelProperty(value="身份证正面图片")
    private String imgPositive;

    @ApiModelProperty(value="身份证反面图片")
    private String imgReverse;
}

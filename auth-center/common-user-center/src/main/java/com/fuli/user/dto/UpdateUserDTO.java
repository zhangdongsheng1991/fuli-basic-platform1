package com.fuli.user.dto;

import com.fuli.user.utils.RegexUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;

/**
 * app_user
 * @author WFZ 2019-09-05
 */
@Data
public class UpdateUserDTO {

    @ApiModelProperty(value = "用户名，系统账号")
    @Length(max = 30 , message = "用户名最多支持输入{max}个字符")
    private String username;

    @ApiModelProperty(value = "头像地址")
    private String headImgUrl;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty(value = "性别：0-默认值未知，1-男，2-女，3-保密",hidden = true)
    private Integer gender;

    @ApiModelProperty(value = "证件类型1.身份证，2护照号",hidden = true)
    private Integer certificateType;

    @ApiModelProperty(value = "证件号码",hidden = true)
    private String certificateCard;

}
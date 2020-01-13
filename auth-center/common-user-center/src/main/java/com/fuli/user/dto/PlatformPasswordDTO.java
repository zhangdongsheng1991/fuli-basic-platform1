package com.fuli.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 账户密码同步到中台dto
 * @Author: WS
 * @CreateDate: 2019/5/15 19:31
 * @Version: 1.0
 */
@Data
public class PlatformPasswordDTO {
    /**
     * 用户id
     */
    private String thirdUserId;

    /**
     * 用户账户(登录账号)
     */
    private String loginName;

    /**
     * 密码(存数据库的密码)
     * 1.本地用户注册时的密码 要同步到中台
     * 2.本地用户修改密码时的新密码 也要同步到中台
     * 3.非本地用户修改密码 不记得原密码时 通过本地手机验证码后传的新密码 这种情况下不需要调用中台检查密码的接口  直接调用同步密码接口
     */
    private String password;

    /**
     * 原密码
     * 非本地用户在本地系统登录修改密码且记得原密码的情况下传 且有这个原密码时就必须要调用中台的检查密码接口ok后 在调用同步密码接口
     */
    private String oldPassword;

    @ApiModelProperty(value="用户来源")
    private String userFrom;
}

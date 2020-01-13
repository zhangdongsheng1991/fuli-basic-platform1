package com.fuli.user.dto;

import com.fuli.user.utils.RegexUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @Description: 更换手势密码请求类
 * @Author: WFZ
 * @CreateDate: 2019/8/2 16:25
 * @Version: 1.0
 */
@Data
public class ChangeGesturePwdDTO implements Serializable {

    @ApiModelProperty(value = "手势密码", required = true)
    @NotBlank(message = "手势密码不能为空")
    private String gesturePassword;

    @ApiModelProperty(value = "手势开关", required = true)
    private Integer gestureSwitch;

    @ApiModelProperty(value = "是否显示", required = true)
    private Integer isDisplayTrajectory;
}

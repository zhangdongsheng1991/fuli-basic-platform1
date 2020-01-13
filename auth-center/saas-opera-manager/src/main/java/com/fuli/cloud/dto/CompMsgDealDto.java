package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class CompMsgDealDto implements Serializable {

    private static final long serialVersionUID = -5761216658746948680L;

    @ApiModelProperty(value = "留言ID",required = true)
    @NotNull(message = "留言ID不能为空")
    private Long id;

    @ApiModelProperty(value = "处理结果 1：有效反馈；2：无效反馈",required = true)
    @NotNull(message = "处理结果不能为空")
    @Range(min=1,max = 2,message = "请传入正确的处理结果")
    private Short state;

    @ApiModelProperty("备注")
    @Size(max=200, message = "备注长度不能超过{max}")
    private String remark;



}

package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 根据用户id获取管理的企业列表
 * @author WFZ
 * @date 2019/12/4
 */
@Data
public class ListCompanyManagerByUserIdDTO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty(value = "用户id",example = "1",required = true)
    @NotNull(message = "用户id不能为空")
    private Integer userId;

    @ApiModelProperty(value = "业务线id，10-薪发放",example = "10",required = true)
    @NotNull(message = "业务线id不能为空")
    private Integer moduleId;

    @ApiModelProperty(value = "是否主题企业。1-是，2-否",example = "2")
    private Integer type;

    @ApiModelProperty(value = "企业id")
    private Long companyId;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "页码，默认值1- 为0表示不分页", example = "1")
    @Min(value = 0, message = "页数最小为0")
    private Integer pageNum = 1;

    @ApiModelProperty(value = "每页数据条数，默认值10", example = "10")
    private Integer pageSize = 10;

}

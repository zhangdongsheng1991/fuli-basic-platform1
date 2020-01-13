package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 组织架构响应类
 * @author yhm
 * @date 2019/06/25
 */
@Data
public class OrganazationVo implements Serializable {

    private static final long serialVersionUID = -2320456555098620197L;
    @ApiModelProperty(name = "id",value = "ID")
    private Integer id;

    @ApiModelProperty(value = "名称")
    private String label;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "上级ID")
    private Integer parentId;


    @ApiModelProperty(value="子级部门列表")
    private List<OrganazationVo> children;

    @ApiModelProperty("部门名称")
    String deptName;

    @ApiModelProperty("部门ID")
    String deptId;

    @ApiModelProperty("1:部门,2:岗位")
    Integer flag;

}
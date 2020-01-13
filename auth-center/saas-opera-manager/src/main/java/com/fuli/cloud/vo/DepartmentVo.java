package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 部门响应类
 * @author yhm
 * @date 2019/06/25
 */
@Data
public class DepartmentVo implements Serializable {

    private static final long serialVersionUID = 1186425258281176506L;

    @ApiModelProperty(name = "id",value = "部门ID")
    private Integer id;

//    @ApiModelProperty(value = "企业ID")
//    private Long companyId;

    @ApiModelProperty(value = "部门名称")
    private String name;

    @ApiModelProperty(value = "部门名称（用于前端组件使用）")
    private String label;

    @ApiModelProperty(value = "部门编码")
    private String code;

    @ApiModelProperty(value = "上级部门")
    private Integer parentId;

    @ApiModelProperty(value = "状态 0：禁用；1：启用")
    private Byte status;

    @ApiModelProperty(value = "操作账号")
    private String operationAccount;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value="部门人数")
    private Integer employeeCount;

    @ApiModelProperty(value="子级部门列表")
    private List<DepartmentVo> children;

//    @JsonIgnore
//    @ApiModelProperty(value="子级部门数",hidden = true)
//    private Integer childCount;

//    @JsonIgnore
//    @ApiModelProperty(value="子级部门ID",hidden = true)
//    private String childIds;

    @ApiModelProperty(value = "岗位列表")
    private List<PositionVo> positionList;

    @ApiModelProperty("上级部门名称")
    String parentName;

    String path;

//    @ApiModelProperty("部门和项目标识：0:部门，1:项目")
//    Integer projectFlag;

}
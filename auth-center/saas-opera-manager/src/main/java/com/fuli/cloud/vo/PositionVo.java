package com.fuli.cloud.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 岗位响应类
 * @author yhm
 * @date 2019/06/27
 */
@Data
public class PositionVo implements Serializable {

    private static final long serialVersionUID = -761842984500244384L;

    @ApiModelProperty(value = "岗位ID")
    private Integer id;

    @ApiModelProperty(value = "部门ID")
    private Integer departmentId;

    @ApiModelProperty(value="岗位名称")
    private String name;

    @ApiModelProperty(value="岗位名称（用于前端组件使用）")
    private String label;

    @ApiModelProperty(value = "岗位编码")
    private String code;

    @ApiModelProperty(value = "父级岗位ID")
    private Integer parentId;

//    @JsonIgnore
    @ApiModelProperty(value="父级路径",hidden = true)
    private String path;

    @ApiModelProperty(value = "状态 0：禁用；1：启用")
    private Byte status;

    @ApiModelProperty(value = "操作人")
    private String operationAccount;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "员工数")
    private Integer employeeCount;

    @ApiModelProperty(value="子级岗位列表")
    private List<PositionVo> children;

//    @JsonIgnore
//    @ApiModelProperty(value="子级岗位数",hidden = true)
//    private Integer childCount;

    @ApiModelProperty("父级岗位名称")
    private String parentName;

}
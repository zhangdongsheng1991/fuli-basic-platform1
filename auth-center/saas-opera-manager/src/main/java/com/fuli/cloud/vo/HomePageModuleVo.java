package com.fuli.cloud.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xq
 */
@Data
public class HomePageModuleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * saas首页服务模块表
     */
    @ApiModelProperty(name = "id" ,value = "主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 所属服务模块
     */
    @ApiModelProperty(name = "moduleId" ,value = "所属服务id")
    private Integer moduleId;

    /**
     * 模块名称
     */
    @ApiModelProperty(name = "name" ,value = "模块名称")
    private String name;


    @ApiModelProperty(name = "type" ,value = "区分列；1-左，2-中，3-右")
    private Integer type;

    /**
     * 排序值
     */
    @ApiModelProperty(name = "sort" ,value = "排序值")
    private Integer sort;

    /**
     * 状态；1-开启，2-关闭
     */
    @ApiModelProperty(name = "state" ,value = "状态；1-开启，2-关闭")
    private Integer state;


    @ApiModelProperty(name = "isDefault" ,value = "是否默认模块；0-是，1-否")
    private Integer isDefault;

    /**
     * 操作人
     */
    @ApiModelProperty(name = "operationAccount" ,value = "操作人/创建人")
    private String operationAccount;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "createTime" ,value = "创建时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 备注
     */
    @ApiModelProperty(name = "remark" ,value = "备注")
    private String remark;

    @ApiModelProperty("工作台数据权限是否选中 true选中 false 未选中")
    private boolean checked;

}

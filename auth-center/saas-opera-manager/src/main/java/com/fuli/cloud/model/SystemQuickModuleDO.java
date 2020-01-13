package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fuli.cloud.constant.DateConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    工作台快捷功能
 * @Author:         WFZ
 * @CreateDate:     2019/4/15 16:30
 * @Version:        1.0
 */
@Data
@TableName("system_quick_module")
public class SystemQuickModuleDO implements Serializable {

    private static final long serialVersionUID = 8479611688814729565L;


    @ApiModelProperty(value = "主键",hidden = true)
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id",hidden = true)
    private Integer userId;

    @ApiModelProperty(value = "模块id",required = true)
    @NotNull(message = "模块id不能为空")
    @Min(value = 0,message = "模块id不能为空")
    private Integer moduleId;

    @ApiModelProperty(value = "排序值",hidden = true)
    private Integer sort;

    @ApiModelProperty(value = "创建时间",hidden = true)
    @JsonFormat(pattern = DateConstant.DATE_FORMART_DATETIME,timezone = DateConstant.TIME_ZONE)
    private Date createTime;

}

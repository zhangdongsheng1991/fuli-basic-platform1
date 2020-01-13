package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门实体类
 * @author yhm
 * @date 2019/06/25
 */
@Data
@TableName("system_department")
@FieldNameConstants
public class SystemDepartment implements Serializable {

    private static final long serialVersionUID = -250759943540433947L;

	@TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(name = "name" , value = "部门名称")
    private String name;

    @ApiModelProperty(name = "code" , value = "部门编码")
    private String code;

    @ApiModelProperty(name = "path" , value = "上级路径")
    private String path;

    @ApiModelProperty(name = "parentId" , value = "上级id")
    @TableField(value = "parent_id")
    private Integer parentId;

    @ApiModelProperty(name = "status" , value = "部门状态 1 启用 0 禁用 ")
    private Byte status;

    @ApiModelProperty(name = "operationAccount" , value = "操作人")
    @TableField(value = "operation_account")
    private String operationAccount;

    @ApiModelProperty(name = "companyId" , value = "更新时间")
    @TableField(value="update_time")
    private Date updateTime;

    @ApiModelProperty(name = "companyId" , value = "创建时间")
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private String[] projectManagerIds;

	public boolean checkIsRoot() {
		return this.parentId == null ? false : this.parentId == 0;
	}
}
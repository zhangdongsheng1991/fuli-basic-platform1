package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
 *  运营用户与模块、企业数据权限关联表
 * @author WFZ 2019-12-04
 */
@Data
@TableName("system_user_grant_company")
@FieldNameConstants
public class SystemUserGrantCompanyDO implements Serializable {

    private static final long serialVersionUID = -1L;

    @ApiModelProperty("主键id")
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty("企业id")
    private Long companyId;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("模块id（业务线id）")
    private Integer moduleId;

    @ApiModelProperty("模块名称(业务线名称)")
    private String moduleName;

    @ApiModelProperty("运营用户id")
    private Integer userId;

    @ApiModelProperty("操作人账号")
    private String operationAccount;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    public SystemUserGrantCompanyDO() {
    }

}


package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *  用户表
 * @author WFZ 2019-07-29
 */
@Data
@TableName("system_user")
@FieldNameConstants
public class SystemUserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户表 - 主键id
     */
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 系统账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户手机号
     */
    private String phoneNumber;

    /**
     * 身份证号码
     */
    private String certificateCard;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态(1.在职，0.离职，-1.删除)
     */
    private Integer status;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 岗位id
     */
    private Integer positionId;

    /**
     * 性别 0-默认值未知，1-男，2-女，3-保密
     */
    private Integer gender;

    /**
     * 是否开通运营权限 0-不开通，1-开通
     */
    private Integer openSystem;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String createOperator;

    /**
     * 创建人id
     */
    private Integer createOpId;

    /**
     * 入职时间
     */
    private Date employmentDate;
    /**
     * 离职时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date dimissionDate;

    /**
     * 操作人id
     */
    private Integer opId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 操作人用户账号
     */
    private String operationAccount;

    /**
     * 备注
     */
    private String remark;

    public SystemUserDO() {
    }

    @TableField(exist = false)
    @ApiModelProperty(value = "角色id集合")
    private Set<Integer> roleIds;

    /**
     * 是否在职
     *
     * @return
     */
    @JsonIgnore
    public boolean isInService() {
        return this.status != null && this.status == 1;
    }


    /**
     * <pre>
     * Description: 离职
     * </pre>
     *
     * @author chenyi
     * @date 20:54 2019/7/30
     **/
    public void dimission() {
        this.status = 0;
        this.openSystem = 0;
        this.dimissionDate = new Date();
    }
}
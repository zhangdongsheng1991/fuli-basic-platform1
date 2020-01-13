package com.fuli.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 员工表-推送中台记录
 * @author WFZ 2019-10-25
 */
@Data
@ApiModel("员工表-推送中台记录")
@TableName("data_record_employee")
public class DataRecordEmployeeDO {

    @ApiModelProperty("主键")
    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    @ApiModelProperty("员工id")
    private Long employeeId;

    @ApiModelProperty("user_id")
    private Long userId;

    @ApiModelProperty("company_id")
    private Long companyId;

    @ApiModelProperty("手机号码")
    private String phoneNumber;

    @ApiModelProperty("证件号码")
    private String certificateCard;

    @ApiModelProperty("真实姓名")
    private String name;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("性别：0-默认值未知，1-男，2-女，3-保密")
    private Integer gender;

    @ApiModelProperty("用户状态:(1.正常，0.离职 ，-1.删除)")
    private Integer status;

    @ApiModelProperty("证件类型1.身份证，2护照号")
    private Integer certificateType;

    @ApiModelProperty("新用户来源 - 各业务线首字母")
    private String userFrom;

    @ApiModelProperty("入职日期")
    private Date employmentDate;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("数据类型，1 - 新增，2 - 更新")
    private Integer type;

    @ApiModelProperty("同步状态：0 - 未同步 ， 1 - 已同步")
    private Integer syncState;

    @ApiModelProperty("中台返回的完整信息")
    private String inParam;

    @ApiModelProperty("失败次数")
    private Boolean failNum;

    public DataRecordEmployeeDO() {
    }

}
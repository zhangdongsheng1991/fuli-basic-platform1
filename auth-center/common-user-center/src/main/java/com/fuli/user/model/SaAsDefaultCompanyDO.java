package com.fuli.user.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:    SaaS门户默认企业展示表
 * @Author:         WFZ
 * @CreateDate:     2019/8/5 18:09
 * @Version:        1.0
*/
@Data
@TableName("saas_default_company")
public class SaAsDefaultCompanyDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * SaaS门户默认企业展示表 - 员工id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    @TableId(value="employee_id",type= IdType.INPUT)
    private Long employeeId;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 创建时间
     */
    private Date createTime;

    public SaAsDefaultCompanyDO() {
    }
}

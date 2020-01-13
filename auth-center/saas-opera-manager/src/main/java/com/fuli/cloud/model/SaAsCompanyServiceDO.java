package com.fuli.cloud.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 *  SaaS企业开通的服务中间表
 * @author WFZ 2019-07-30
 */
@Data
@TableName("saas_company_service")
public class SaAsCompanyServiceDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 服务id
     */
    private Integer serviceId;

    /**
     * 状态；1-开启，2-关闭
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 关闭时间
     */
    private Date shutTime;

    public SaAsCompanyServiceDO() {
    }

}

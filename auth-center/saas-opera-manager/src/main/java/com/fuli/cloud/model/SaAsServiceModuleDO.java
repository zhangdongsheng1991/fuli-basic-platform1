package com.fuli.cloud.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  现有第三方服务模块记录
 * @author WFZ 2019-07-30
 */
@Data
@TableName("saas_service_module")
public class SaAsServiceModuleDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value="id",type= IdType.AUTO)
    private Integer id;

    /**
     * 对应权限的模块id
     */
    private Integer moduleId;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 服务状态：1-开启，2-关闭
     */
    private Integer state;

    /**
     * 是否第三方服务：1-是，0-否
     */
    private Integer isOther;

    /**
     * 标识，对应前端页面
     */
    private String label;

    /**
     * 服务logo地址-已开通
     */
    private String logoUrl;

    /**
     * 服务logo地址-未开通
     */
    private String logoUrlNot;

    /**
     * 服务类型 -- 薪发放业务线需求
     */
    private String serviceType;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    public SaAsServiceModuleDO() {
    }

}

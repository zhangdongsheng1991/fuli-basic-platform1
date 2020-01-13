package com.fuli.cloud.vo;

import lombok.Data;

/**
 * 员工导入获取企业部门和岗位名
 * @author AnJiao
 * @date 2019/7/8
 */
@Data
public class PositionNameAndDepartmentNameVo {
    /**
     * 部门名
     */
    private String dName;
    /**
     * 部门id
     */
    private Integer dId;
    /**
     * 岗位名
     */
    private String pName;
    /**
     * 岗位id
     */
    private Integer pId;
}

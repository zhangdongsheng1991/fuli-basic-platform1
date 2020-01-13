package com.fuli.cloud.dto;

import lombok.Data;

import java.util.Date;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/28
 */
@Data
public class EmpCountDTO {


    // 员工id
    private Integer id;
    private Integer deptId;
    private Integer positionId;
    private Date firtSecoundOfMonth;
    private Date lastSecoundOfMonth;
    // 1 在职 ，0 离职
    private Integer status;
}

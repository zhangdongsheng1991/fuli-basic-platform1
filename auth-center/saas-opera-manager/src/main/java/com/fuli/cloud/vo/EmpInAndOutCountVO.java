package com.fuli.cloud.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <pre>
 * Description:
 * </pre>
 *
 * @author chenyi
 * @date 2019/8/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpInAndOutCountVO {
    @ApiModelProperty("在职员工人数")
    private int inCount;

    @ApiModelProperty("离职员工人数")
    private int outCount;
}

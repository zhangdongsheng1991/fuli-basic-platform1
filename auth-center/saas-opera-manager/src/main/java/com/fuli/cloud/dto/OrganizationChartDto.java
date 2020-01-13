package com.fuli.cloud.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * @author chenyi
 * @date 2019年6月27日下午2:49:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrganizationChartDto implements Serializable {

	private static final long serialVersionUID = -9191854582829960589L;

	@ApiModelProperty(value = "页码", example = "1")
	@Range(min = 1, max = 1000, message = "页码参数必须在{min}-{max}之间")
	private Integer pageNo = 1;

	@ApiModelProperty(value = "每页数据条数", example = "6")
	@Range(min = 1, max = 100, message = "页大小参数必须在{min}-{max}之间")
	private Integer pageSize = 10;

	@Min(value = 0, message = "部门id不能为负数")
	@ApiModelProperty(value = "部门id")
	private Integer deptId;

	@Min(value = 0, message = "岗位id不能为负数")
	@ApiModelProperty(value = "岗位id")
	private Integer positionId;


	@Min(value = 0, message = "状态标记不合法")
	@Max(value = 1, message = "状态标记不合法")
	@ApiModelProperty(value = "状态(1:在职,0:离职)")
	private Integer status;

	@ApiModelProperty(value = "部门名称")
	private String deptName;

	@ApiModelProperty(value = "岗位名称")
	private String positionName;

	@ApiModelProperty(value = "员工姓名")
	private String empName;

	@ApiModelProperty(value = "是否数据权限查询")
	private Integer isAuthQuery;

}

package com.fuli.cloud.vo.employee;

import com.fuli.cloud.commons.annotation.ListHeader;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 
 * <p>
 * Description: 组织架构树管理 人员列表vo
 * </p>
 * 
 * @author chenyi
 * @date 2019年6月29日上午9:59:35
 */
@Data
@FieldNameConstants
public class EmpDeptPositionVo {

	@ApiModelProperty("员工id")
	private String id;

	@ListHeader(headerName = "姓名")
	@ApiModelProperty("姓名")
	private String name;

	@ListHeader(headerName = "电话")
	@ApiModelProperty("电话")
	private String phoneNumber;

	@ListHeader(headerName = "身份证")
	@ApiModelProperty("身份证")
	private String certificateCard;

	@ListHeader(headerName = "部门")
	@ApiModelProperty("部门名称")
	private String deptName;

	@ListHeader(headerName = "岗位")
	@ApiModelProperty("岗位名称")
	private String positionName;

	@ListHeader(headerName = "工作状态")
	@ApiModelProperty("工作状态")
	private Integer status;
}

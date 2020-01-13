package com.fuli.user.commons;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @NoArgsConstructor  生成全参构造函数
 * @NoArgsConstructor  生成无参构造函数
 *
 * @Description:    分页响应公共类
 * @Author:         FZ
 * @CreateDate:     2019/4/26 14:31
 * @Version:        1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;


	@ApiModelProperty(name = "totalPage",value = "总页数")
	private int totalPage;

	@ApiModelProperty(name = "total",value = "总条数")
	private long total;

	@ApiModelProperty(name = "records",value = "数据列表")
	private Object records;

	@ApiModelProperty(name = "currentPage",value = "当前页数")
	private int currentPage;

	@ApiModelProperty(name = "pageSize",value = "每页显示的条数")
	private int pageSize;

}

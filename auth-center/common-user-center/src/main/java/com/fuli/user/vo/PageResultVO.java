package com.fuli.user.vo;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


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
public class PageResultVO<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;


	@ApiModelProperty(value = "总页数")
	private int totalPage;

	@ApiModelProperty(value = "总条数")
	private Long total;

	@ApiModelProperty(value = "数据列表")
	private List<T> records;

	@ApiModelProperty(value = "当前页数")
	private int currentPage;

	@ApiModelProperty(value = "每页显示的条数")
	private int pageSize;

	/**
	 * 封装返回的分页参数
	 * @author      WFZ
	 * @param
	 * @return
	 * @exception
	 * @date        2019/5/16 10:07
	 */
	public static PageResultVO getPageResult(PageInfo pageInfo){
		return PageResultVO.builder().records(pageInfo.getList()).total(pageInfo.getTotal()).totalPage(pageInfo.getPages()).currentPage(pageInfo.getPageNum()).pageSize(pageInfo.getSize()).build();
	}

}

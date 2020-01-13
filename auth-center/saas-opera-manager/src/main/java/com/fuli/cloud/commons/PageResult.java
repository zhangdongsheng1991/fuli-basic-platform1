package com.fuli.cloud.commons;

import com.fuli.cloud.vo.TableHeaderVO;
import com.github.pagehelper.Page;
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
public class PageResult<T> implements Serializable {

	private static final long serialVersionUID = -275582248840137389L;


	@ApiModelProperty(name = "totalPage",value = "总页数")
	private long totalPage;

	@ApiModelProperty(name = "total",value = "总条数")
	private long total;

	@ApiModelProperty(name = "records",value = "数据列表")
	private List<T> records;

	@ApiModelProperty(name = "currentPage",value = "当前页数")
	private int currentPage;

	@ApiModelProperty(name = "pageSize",value = "每页显示的条数")
	private int pageSize;

	@ApiModelProperty(name = "tableHeaders",value = "自定义列")
	private List<TableHeaderVO> tableHeaders;

	/**
	 * 封装返回的分页参数
	 * @author      WFZ
	 * @param		pageInfo
	 * @return		PageResult
	 * @date        2019/5/16 10:07
	 */
	public static PageResult getPageResult(PageInfo pageInfo,List<TableHeaderVO> tableHeaders){
		return PageResult.builder().records(pageInfo.getList()).total(pageInfo.getTotal()).totalPage(pageInfo.getPages()).currentPage(pageInfo.getPageNum()).pageSize(pageInfo.getSize()).tableHeaders(tableHeaders).build();
	}

	public static PageResult getPageResult(Page page, List<TableHeaderVO> tables){
		return PageResult.builder().records(page.getResult()).total(page.getTotal()).totalPage(page.getPages()).currentPage(page.getPageNum()).pageSize(page.getPageSize()).tableHeaders(tables).build();
	}

	/**
	 * 封装返回的分页参数
	 * @author      WFZ
	 * @param
	 * @return
	 * @exception
	 * @date        2019/5/16 10:07
	 */
	public static  PageResult getPageResult(PageInfo pageInfo){
		return PageResult.builder().records(pageInfo.getList()).total(pageInfo.getTotal()).totalPage(pageInfo.getPages()).currentPage(pageInfo.getPageNum()).pageSize(pageInfo.getSize()).build();
	}

	public static PageResult getPageResult(Page page){
		return PageResult.builder().records(page.getResult()).total(page.getTotal()).totalPage(page.getPages()).currentPage(page.getPageNum()).pageSize(page.getPageSize()).build();
	}
}

package com.fuli.cloud.commons.utils;

import com.fuli.cloud.commons.PageResult;
import com.fuli.cloud.commons.annotation.ListHeader;
import com.fuli.cloud.vo.TableHeaderVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 
 * <p>
 * Description: 根据注解生产列表list
 * </p>
 * 
 * @author chenyi
 * @date 2019年7月5日下午2:15:44
 */
public abstract class ListHeaderUtil {

	public static <T> void setListHeaders(PageResult<T> pageResult, Class<T> voClass) {
		pageResult.setTableHeaders(getListHeaders(voClass));
	}

	/**
	 * 
	 * <p>
	 * Description: 根据注解生产列表list
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年7月5日下午2:58:21
	 * @param <T>
	 * @param voClass
	 * @return
	 */
	public static <T> List<TableHeaderVO> getListHeaders(Class<T> voClass) {

		List<TableHeaderVO> tableHeaders = Lists.newLinkedList();
		Field[] fs = voClass.getDeclaredFields();
		for (Field f : fs) {
			f.setAccessible(true);
			if (skipField(f)) {
				continue;
			}
			ListHeader annotation = f.getAnnotation(ListHeader.class);
			String headerName = annotation.headerName();
			String headerFieldName = annotation.headerFieldName();
			if (StringUtils.isBlank(headerFieldName)) {
				headerFieldName = f.getName();
			}
			tableHeaders.add(new TableHeaderVO(headerFieldName, headerName));
		}
		return tableHeaders;
	}

	private static boolean skipField(Field f) {
		// 跳过静态字段
		if (Modifier.isStatic(f.getModifiers())) {
			return true;
		}
		// 没加注解的字段跳过
		ListHeader annotation = f.getAnnotation(ListHeader.class);
		return annotation == null;
	}

}

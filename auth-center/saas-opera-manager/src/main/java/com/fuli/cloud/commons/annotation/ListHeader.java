package com.fuli.cloud.commons.annotation;

import java.lang.annotation.*;

/**
 * 
 * <p>
 * Description: 列头注解
 * </p>
 * 
 * @author chenyi
 * @date 2019年7月5日上午11:42:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.FIELD)
public @interface ListHeader {
	/**
	 * 
	 * <p>
	 * Description: 列头名称
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年7月5日上午11:44:14
	 * @return
	 */
	String headerName();

	/**
	 * 
	 * <p>
	 * Description: 列头字段名称，默认取vo字段名称
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年7月5日下午2:54:57
	 * @return
	 */
	String headerFieldName() default "";

	/**
	 * 
	 * <p>
	 * Description: 字段顺序, 默认按字段定义顺序
	 * </p>
	 * 
	 * @author chenyi
	 * @date 2019年7月5日上午11:35:20
	 * @return
	 */
	// TODO
	int order() default -1;

	// TODO 分组
}

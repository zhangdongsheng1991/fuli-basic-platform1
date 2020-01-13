package com.fuli.cloud.commons.utils;

import org.springframework.util.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author FZ
 */
public class DateUtil {

	/**
	 * 设置年
	 *
	 * @param date   时间
	 * @param amount 年数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setYears(Date date, int amount) {
		return set(date, Calendar.YEAR, amount);
	}

	/**
	 * 设置月
	 *
	 * @param date   时间
	 * @param amount 月数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setMonths(Date date, int amount) {
		return set(date, Calendar.MONTH, amount);
	}

	/**
	 * 设置周
	 *
	 * @param date   时间
	 * @param amount 周数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setWeeks(Date date, int amount) {
		return set(date, Calendar.WEEK_OF_YEAR, amount);
	}

	/**
	 * 设置天
	 *
	 * @param date   时间
	 * @param amount 天数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setDays(Date date, int amount) {
		return set(date, Calendar.DATE, amount);
	}
	
	/**
	 * 增加一天
	 * @param date   时间
	 * @return 设置后的时间
	 */
	public static Date incrementOneDay(Date date) {
		return set(date, Calendar.DATE, 1);
	}

	/**
	 * 设置小时
	 *
	 * @param date   时间
	 * @param amount 小时数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setHours(Date date, int amount) {
		return set(date, Calendar.HOUR_OF_DAY, amount);
	}

	/**
	 * 设置分钟
	 *
	 * @param date   时间
	 * @param amount 分钟数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setMinutes(Date date, int amount) {
		return set(date, Calendar.MINUTE, amount);
	}

	/**
	 * 设置秒
	 *
	 * @param date   时间
	 * @param amount 秒数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setSeconds(Date date, int amount) {
		return set(date, Calendar.SECOND, amount);
	}

	/**
	 *  减少一秒
	 *
	 * @param date   时间
	 * @return 设置后的时间
	 */
	public static Date decreaseOneSecond(Date date) {
		return set(date, Calendar.SECOND, -1);
	}
	
	/**
	 * 设置毫秒
	 *
	 * @param date   时间
	 * @param amount 毫秒数，-1表示减少
	 * @return 设置后的时间
	 */
	public static Date setMilliseconds(Date date, int amount) {
		return set(date, Calendar.MILLISECOND, amount);
	}

	/**
	 * 设置日期属性
	 *
	 * @param date          时间
	 * @param calendarField 更改的属性
	 * @param amount        更改数，-1表示减少
	 * @return 设置后的时间
	 */
	private static Date set(Date date, int calendarField, int amount) {
		Assert.notNull(date, "The date must not be null");
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_DATETIME_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_DATE2 = "yyyy/MM/dd";
	public static final String PATTERN_TIME = "HH:mm:ss";

	public static final ConcurrentDateFormat DATETIME_FORMAT = ConcurrentDateFormat.of(PATTERN_DATETIME);
	public static final ConcurrentDateFormat DATE_FORMAT = ConcurrentDateFormat.of(PATTERN_DATE);
	public static final ConcurrentDateFormat TIME_FORMAT = ConcurrentDateFormat.of(PATTERN_TIME);

	/**
	 * 日期时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatDateTime(Date date) {
		return DATETIME_FORMAT.format(date);
	}

	/**
	 * 日期格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 时间格式化
	 *
	 * @param date 时间
	 * @return 格式化后的时间
	 */
	public static String formatTime(Date date) {
		return TIME_FORMAT.format(date);
	}

	/**
	 * 日期格式化
	 *
	 * @param date    时间
	 * @param pattern 表达式
	 * @return 格式化后的时间
	 */
	public static String format(Date date, String pattern) {
		return ConcurrentDateFormat.of(pattern).format(date);
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param pattern 表达式
	 * @return 时间
	 */
	public static Date parse(String dateStr, String pattern) {
		ConcurrentDateFormat format = ConcurrentDateFormat.of(pattern);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * 将字符串转换为时间
	 *
	 * @param dateStr 时间字符串
	 * @param format  ConcurrentDateFormat
	 * @return 时间
	 */
	public static Date parse(String dateStr, ConcurrentDateFormat format) {
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw Exceptions.unchecked (e);
		}
	}

	/**
	 * 字符串转换为Date ，格式yyyy-MM-dd
	 * @param formatDate
	 * @return
	 */
	public static Date parseDate(String formatDate) {
		return parse(formatDate, DATE_FORMAT);
	}
	
	/**
	 *  格式化为yyyy-MM-dd
	 * @param formatDate
	 * @return 时间
	 */
	public static Date parseDate(Date formatDate) {
		return parseDate(formatDate(formatDate));
	}


	/**
	 * <p>
	 * Description:yyyy-MM-dd HH:mm:ss
	 * </p>
	 *
	 * @return
	 * @author chenyi
	 * @date 2019年7月17日下午12:53:28
	 */
	public static String nowDateTime() {
		return format(new Date(), PATTERN_DATETIME);
	}

	/**
	 * <p>
	 * Description: yyyyMMddHHmmss
	 * </p>
	 *
	 * @return
	 * @author chenyi
	 * @date 2019年7月17日下午12:53:05
	 */
	public static String nowDateTime2() {
		return format(new Date(), PATTERN_DATETIME_YYYYMMDDHHMMSS);
	}


	/**
	 * <pre>
	 * Description: 返回当月第一条 例如:"2019-08-01 00:00:00"
	 * </pre>
	 *
	 * @return 例如："2019-08-01 00:00:00"
	 * @author chenyi
	 * @date 11:35 2019/8/16
	 **/
	public static Date firtSecoundOfMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天   
		String monthStart = format.format(c.getTime()) + " 00:00:00";
		return parse(monthStart, PATTERN_DATETIME);
	}


	/**
	 * <pre>
	 * Description: 返回当月最后一天 例如："2019-08-31 23:59:59"
	 * </pre>
	 *
	 * @return 例如："2019-08-31 23:59:59"
	 * @author chenyi
	 * @date 11:36 2019/8/16
	 **/
	public static Date lastSecoundOfMonth() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String monthEnd = format.format(ca.getTime()) + " 23:59:59";
		return parse(monthEnd, PATTERN_DATETIME);
	}

}

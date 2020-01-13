package com.fuli.user.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具
 *
 * @author WFZ
 */
public class RegexUtil {
	/**
	 * 用户名
	 */
	public static final String USER_NAME = "^[a-zA-Z\\u4E00-\\u9FA5][a-zA-Z0-9_\\u4E00-\\u9FA5]{1,11}$";

	/**
	 * 用户名   ^(?![0-9]+$)(?![_]+$)[a-zA-Z0-9_][a-zA-Z0-9_]{1,30}$
	 */
	public final static String USER_NAMES = "^(?![0-9]+$)(?![_]+$)[a-zA-Z0-9_][a-zA-Z0-9_]{1,30}$";

	/** 判断是否纯数字*/
	public final static String NUMBER = "^[0-9]+$";

	/**
	 * 密码
	 */
	public static final String USER_PASSWORD = "^.{6,14}$";

	/**
	 * 邮箱
	 */
	public static final String EMAIL = "^\\w+([-+.]*\\w+)*@([\\da-z](-[\\da-z])?)+(\\.{1,2}[a-z]+)+$";

	/**
	 * 手机号
	 */
	public static final String PHONE = "^1[3456789]\\d{9}$";

	/**
	 * 手机号或者邮箱
	 */
	public static final String EMAIL_OR_PHONE = EMAIL + "|" + PHONE;

	/**
	 * URL路径
	 */
	public static final String URL = "^(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})(:[\\d]+)?([\\/\\w\\.-]*)*\\/?$";

	/**
	 * 身份证校验，初级校验，具体规则有一套算法
	 */
	public static final String ID_CARD = "^\\d{15}$|^\\d{17}([0-9]|X)$";

	/**
	 * 域名校验
	 */
	public static final String DOMAIN = "^[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,4}$";

	/**
	 * 企业社会统一信用代码
	 */
	public static final String CREDIT_CODE  = "[0-9a-zA-Z]{2}[0-9]{6}[0-9a-zA-Z]{10}$";

	/**
	 * 编译传入正则表达式和字符串去匹配,忽略大小写
	 *
	 * @param regex        正则
	 * @param beTestString 字符串
	 * @return {boolean}
	 */
	public static boolean match(String regex, String beTestString) {
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(beTestString);
		return matcher.matches();
	}

	/**
	 * 编译传入正则表达式在字符串中寻找，如果匹配到则为true
	 *
	 * @param regex        正则
	 * @param beTestString 字符串
	 * @return {boolean}
	 */
	public static boolean find(String regex, String beTestString) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(beTestString);
		return matcher.find();
	}

	/**
	 * 根据身份证的号码算出当前身份证持有者的性别和年龄 18位身份证
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Integer> getCarInfo(String CardCode){
		Map<String, Integer> map = new HashMap<String, Integer>(8);
		map.put("sex", 0);
		try{
			// 得到年份
			String year = CardCode.substring(6).substring(0, 4);
			// 得到月份
			String yue = CardCode.substring(10).substring(0, 2);
			//得到日
			// String day=CardCode.substring(12).substring(0,2);
			int sex = 0;
			// 判断性别
			if (Integer.parseInt(CardCode.substring(16).substring(0, 1)) % 2 == 0) {
				sex = 2;
			} else {
				sex = 1;
			}
			// 得到当前的系统时间
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			// 当前年份
			String nowYear = format.format(date).substring(0, 4);
			// 月份
			String nowMonth = format.format(date).substring(5, 7);
			// String fday=format.format(date).substring(8,10);
			int age = 0;
			// 当前月份大于用户出身的月份表示已过生
			if (Integer.parseInt(yue) <= Integer.parseInt(nowMonth)) {
				age = Integer.parseInt(nowYear) - Integer.parseInt(year) + 1;
			} else {// 当前用户还没过生
				age = Integer.parseInt(nowYear) - Integer.parseInt(year);
			}
			map.put("sex", sex);
			map.put("age", age);
		}catch (Exception e){
			e.printStackTrace();
		}
		return map;
	}


}

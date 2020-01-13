package com.fuli.user.utils;

import com.fuli.user.commons.Result;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenyi
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class PublicUtil {

    /**
     * 判断对象是否Empty(null或元素为0)
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isEmpty(Object pObj) {
        if (pObj == null) {
            return true;
        }
        if (pObj == "") {
            return true;
        }
        if (pObj instanceof String) {
            return ((String) pObj).length() == 0;
        } else if (pObj instanceof Collection) {
            return ((Collection) pObj).isEmpty();
        } else if (pObj instanceof Map) {
            return ((Map) pObj).size() == 0;
        }
        return false;
    }

    /**
     * 判断对象是否为NotEmpty(!null或元素大于0)
     * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
     *
     * @param pObj 待检查对象
     * @return boolean 返回的布尔值
     */
    public static boolean isNotEmpty(Object pObj) {
        if (pObj == null) {
            return false;
        }
        if (pObj == "") {
            return false;
        }
        if (pObj instanceof String) {
            return ((String) pObj).length() != 0;
        } else if (pObj instanceof Collection) {
            return !((Collection) pObj).isEmpty();
        } else if (pObj instanceof Map) {
            return ((Map) pObj).size() != 0;
        }
        return true;
    }


    /**
     * 判断Integer是否为null或者为0
     * @author      WFZ
     * @param       pObj 待检查对象
     * @return      boolean 返回的布尔值
     */
    public static boolean isNotNull(Integer pObj) {
        boolean flag = true;
        if (pObj == null || pObj.intValue()== 0) {
            flag = false;
        }
        return flag;
    }


    /**
     * 判断Long是否为null或者为0
     * @author      WFZ
     * @param       pObj 待检查对象
     * @return      boolean 返回的布尔值
     */
    public static boolean isNotNull(Long pObj) {
        boolean flag = true;
        if (null== pObj || pObj.intValue()==0L) {
            flag = false;
        }
        return flag;
    }

    /**
     * 请求响应码为0 ，则返回true
     * @param result 待检查结果
     * @return boolean 返回的布尔值
     */
	public static boolean isResultSuccess(Result<?> result) {
		if (result == null) {
			log.error("result is null !!! 请求接口返回的result为空");
			return false;
		}
		if (!result.checkIs200()) {
			log.error("result code : " + result.getCode() + " , msg :" + result.getMsg());
			return false;
		}
		return true;
	}
	

    /**
     * 模糊查询特殊字符转意， 只循环前十个字符
     * @author      WFZ
     * @param 	    str:字符串
     * @return      Result
     * @date        2019/6/13 11:48
     */
    public static String removeSpecialCharacters(String str){
        if (StringUtils.isNotBlank(str)){
            final String SPECIAL_CHARACTER = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
            StringBuilder sb = new StringBuilder();
            char[] a = str.toCharArray();
            Pattern p = Pattern.compile(SPECIAL_CHARACTER);
            for (int i = 0; i < (a.length > 10 ? 10 :a.length); i++) {
                String s = String.valueOf(a[i]);
                Matcher m = p.matcher(s);
                if (m.find()){
                    s = "\\"+ s;
                }
                sb.append(s);
            }
            return sb.toString();
        }
        return str;
    }
}

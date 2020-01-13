package com.fuli.cloud.commons.utils;

import com.fuli.cloud.commons.Result;
import com.fuli.cloud.constant.CommonConstant;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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
     * <pre>
     * Description: 去除String字段的前后空格
     * </pre>
     *
     * @param o 待分析对象
     * @author chenyi
     * @date 16:44 2019/8/9
     **/
    public static void trim(Object o) {

        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            // 跳过静态字段
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            if (f.getType() == String.class) {
                try {
                    String value = (String) f.get(o);
                    if (value != null) {
                        f.set(o, value.trim());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    /***
     *  https://blog.csdn.net/u011106915/article/details/76066985
     * @param IDNumber 身份证号
     * @return boolean
     */
    public static boolean isIDNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾
        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {
            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        //System.out.println("身份证最后一位:" + String.valueOf(idCardLast).toUpperCase() +"错误,正确的应该是:" + idCardY[idCardMod].toUpperCase());
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return matches;
    }

    /**
     * 身份证判断男女
     *
     * @param value 身份证
     * @return 0-未知,1-男，2-女
     */
    public static int getSex(String value) {
        if (value == null) {
            return 0;
        }
        value = value.trim();
        if (value.length() != 15 && value.length() != 18) {
            return 0;
        }
        String lastValue;
        /*
         * 15位身份证号码，第15位为性别校验位，奇数为男，偶数为女
         * 18位身份证号码，第17位为性别校验位，奇数为男，偶数为女
         */
        if (value.length() == 15) {
            lastValue = value.substring(value.length() - 1, value.length());
        } else {
            lastValue = value.substring(value.length() - 2, value.length() - 1);
        }

        int sex;
        if (lastValue.trim().toLowerCase().equals("x") || lastValue.trim().toLowerCase().equals("e")) {
            return 1;
        } else {
            sex = Integer.parseInt(lastValue) % 2;
            return sex == 0 ? 2 : 1;
        }

    }

    public static final String XLSX = ".xlsx";
    public static final String XLS = ".xls";

    /**
     * 判断File文件的类型
     *
     * @param fileName 传入的文件
     * @return true 是,false 否
     */
    public static boolean checkFileNameIsExcel(String fileName) {
        if (fileName == null) {
            return false;
        }
        if (fileName.endsWith(XLSX)) {
            return true;
        }
        if (fileName.endsWith(XLS)) {
            return true;
        }
        return false;
    }

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
     * 请求响应码为200 ，则返回true
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
            for (int i = 0; i < (a.length > CommonConstant.SPLIT_LENGTH ? CommonConstant.SPLIT_LENGTH:a.length); i++) {
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

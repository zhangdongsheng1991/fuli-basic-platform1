package com.fuli.task.server.util;

import com.fuli.task.server.model.Result;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;

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


}

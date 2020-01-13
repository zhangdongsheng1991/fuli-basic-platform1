package com.fuli.cloud.commons.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;


/**
 * @Description:    签名拼接
 * @Author:         FZ
 * @CreateDate:     2019/4/25 17:14
 * @Version:        1.0
*/
@Slf4j
public class OrderInfoUtil {
	

	/**
	 * 拼接键值对
	 * 
	 * @param key
	 * @param value
	 * @param isEncode
	 * @return
	 */
	private static String buildKeyValue(String key, String value, boolean isEncode) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		sb.append("=");
		if (isEncode) {
			try {
				sb.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				sb.append(value);
			}
		} else {
			sb.append(value);
		}
		return sb.toString();
	}
	
	/**
	 * 获取签名 -- 薪发放业务线
	 * @param map : 待签名授权信息
	 * @return
	 */
	public static String getSign(Map<String, String> map ) {
		List<String> keys = new ArrayList<String>(map.keySet());
		// key排序
		Collections.sort(keys);

		StringBuilder authInfo = new StringBuilder();
		for (int i = 0; i < keys.size() - 1; i++) {
			String key = keys.get(i);
			String value = map.get(key);
			/** value不为空才加入签名*/
			if(PublicUtil.isNotEmpty(value)){
				authInfo.append(buildKeyValue(key, value, false));
				authInfo.append("&");
			}
		}

		String tailKey = keys.get(keys.size() - 1);
		String tailValue = map.get(tailKey);
		authInfo.append(buildKeyValue(tailKey, tailValue, false));

		String sign = "";
		try {
			/**
			 * 生成签名
			 **/
			sign = Md5Util.MD5Encode(authInfo.toString(), "UTF-8").toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}


	/**CFCA 加签**/
	public static String signMsg(Map<String,Object> salaryMap){
		try{
			//Map<String, Object> paramsTree  = objToMap(param);
			String jsonString = JSON.toJSONString(salaryMap);
			log.info("签名参数"+ jsonString);
			String authorization =  CFCAUtil.signMsg(jsonString);
			return  authorization;
		}catch (Exception e){}
		return null;
	}


	/**
	 * 将对象转成TreeMap,属性名为key,属性值为value
	 * @param object    对象
	 * @return
	 * @throws IllegalAccessException
	 */
	public static TreeMap<String, Object> objToMap(Object object) throws IllegalAccessException {

		Class clazz = object.getClass();
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		while ( null != clazz.getSuperclass() ) {
			Field[] declaredFields1 = clazz.getDeclaredFields();

			for (Field field : declaredFields1) {
				String name = field.getName();

				// 获取原来的访问控制权限
				boolean accessFlag = field.isAccessible();
				// 修改访问控制权限
				field.setAccessible(true);
				Object value = field.get(object);
				// 恢复访问控制权限
				field.setAccessible(accessFlag);
				treeMap.put(name, value);
			}

			clazz = clazz.getSuperclass();
		}
		return treeMap;
	}
}

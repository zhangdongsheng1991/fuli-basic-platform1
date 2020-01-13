package com.fuli.cloud.commons.utils;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.CertUtil;
import cfca.sadk.util.KeyUtil;
import cfca.sadk.util.Signature;
import cfca.sadk.x509.certificate.X509Cert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fuli.cloud.commons.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Title: CFCAUtil.java
 * @Package com.yinglian.common.cfca
 * @Description: TODO
 * @author funny
 * @date 2018年11月1日 上午9:23:07
 */
@Slf4j
public class CFCAUtil {

	/**
	 * 获取证书对象
	 */
	private static X509Cert signCert = null;

	/**
	 * 获得私钥对象
 	 */
	private static PrivateKey privateKey = null;

	/**
	 * 签名算法
 	 */
	public static final String SIG_NALG = Mechanism.SHA256_RSA;

	private static Session session;

	private static final String CFCA_PATH = "conf/";

	/**
	 * pfx证书文件
 	 */
	private static final String PFX_FILE_PATH = "test.pfx";

	/**
	 * pfx密码
 	 */
	private static final String PFX_FILE_PWD = "cfca1234";

	/**
	 *加签字段
	 */
	public static final String SIGNATURE = "signature";

	static {
		try {
			// 步骤1 加密库初始化
			String deviceName = JCrypto.JSOFT_LIB;
			JCrypto.getInstance().initialize(deviceName, null);
			session = JCrypto.getInstance().openSession(deviceName);

			// 步骤2 构建证书对象
			String fileName = "classpath:/" + CFCA_PATH + PFX_FILE_PATH;
			ResourceLoader resourceLoader = new DefaultResourceLoader();
			InputStream fileInputStream = resourceLoader.getResource(fileName).getInputStream();
			signCert = CertUtil.getCertFromPFX(fileInputStream,PFX_FILE_PWD);

			InputStream inputStream=CFCAUtil.class.getClassLoader().getResourceAsStream(CFCA_PATH + PFX_FILE_PATH);
			// 步骤3 获得私钥对象
			privateKey = KeyUtil.getPrivateKeyFromPFX(inputStream,PFX_FILE_PWD);
		} catch (Exception e) {
			log.info("Read key error");
			e.printStackTrace();
			throw new RuntimeException("获得私钥对象发生错误[" + e.getMessage() + "]");
		}
	}

	public static String getRealPath(String cerPath) {
		String url = CFCAUtil.class.getClassLoader().getResource(cerPath).getFile();
		return url;
	}

	/**
	 * 加签 P7消息签名(签名结果值[Base64])
	 *
	 * @param sourData
	 */
	public static String signMsg(String sourData) throws Exception {
		try {
			Signature signKit = new Signature();
			byte[] message = sourData.getBytes("UTF8");
			byte[] signedData = signKit.p7SignMessageDetach(SIG_NALG, message, privateKey, signCert, session);
			return new String(signedData);
		} catch (Exception e) {
			throw new RuntimeException("签名失败：", e);
		}
	}

	/**
	 * 按照键排序
	 * @param map
	 */
	public static void sortByKey(Map map) {
		Object[] objects = map.keySet().toArray();
		Arrays.sort(objects);
		for (int i = 0; i < objects.length; i++) {
			System.out.println("键：" + objects[i] + " 值：" + map.get(objects[i]));
		}
	}

	/**
	 * 让 Map按key升序进行排序
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> params) {
		return new TreeMap<String, Object>(params);
	}

	/**
	 * 验签
	 * @param signMsg 加签字符串
	 * @param jsonRequestParam 请求的json字符串
	 * @return
	 */
	public static boolean checkSignMsg(String signMsg, Object jsonRequestParam) {
		boolean result = false;
		try {
			Signature signKit = new Signature();
			byte[] p7SignedData = signMsg.getBytes();
			String buSiJson="";
			if(jsonRequestParam instanceof String) {
				buSiJson= jsonRequestParam.toString();
			}else {
				buSiJson= JSONObject.toJSONString(jsonRequestParam);
			}
			byte[] sourceData = buSiJson.getBytes("UTF8");
			result = signKit.p7VerifyMessageDetach(sourceData, p7SignedData, session);
		} catch (Exception e) {
			throw new RuntimeException("验签失败：", e);
		}
		return result;
	}

	/**
	 *
	 * <b>方法描述：</b> 请求参数排序验签
	 *
	 * @param reqJsonString
	 * @return void
	 * @date 2019/8/5 15:14
	 * @version 1.0.0
	 */
	public static void vaLiStringSignature(String reqJsonString) throws Exception{
		boolean validSignature = false;
		try {
            //请求报文的签名字符串
			String reqSignature = null;
			Map reqMap = (Map) JSON.parse(reqJsonString);
			Iterator<Map.Entry<String, Object>> it = reqMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				Object val = entry.getValue();
				if (SIGNATURE.equals(entry.getKey())) {
					reqSignature = val.toString();
					it.remove();//移除签名的签名串
				}
                //对请求数据存在list数据处理
				if(val instanceof JSONArray) {
					JSONArray array = (JSONArray) val;
					JSONArray sortAfter = new JSONArray();
					for (int i = 0; i < array.size(); i++) {
						Map<String, Object> resMap = sortMapByKey((Map<String, Object>) JSON.parse(array.getString(i)));
						sortAfter.add(resMap);
					}
					reqMap.put(entry.getKey(), sortAfter);
				}
			}
			//使用cfca的软证书方式进行接口验签
			Map<String, Object> sortMap = CFCAUtil.sortMapByKey(reqMap);
			log.info("排序后的json请求字符串:{}", JSON.toJSONString(sortMap));
			log.info("签名后:{}", CFCAUtil.signMsg(JSON.toJSONString(sortMap)));
			validSignature = CFCAUtil.checkSignMsg(reqSignature,sortMap);
			log.info("验证状态：{}", validSignature);
		} catch (Exception e) {
			log.error("解析请求报文签名验证失败, {}", e.getMessage());
		}
		if(!validSignature) {
			throw new ServiceException(-1,"验证失败");
		}
	}

}

package com.fuli.cloud.commons.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @Description:    MD5工具类
 * @Author:         WFZ
 * @CreateDate:     2019/5/24 15:05
 * @Version:        1.0
*/
public class Md5Util {
	
	
	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	protected static MessageDigest messagedigest = null;
	static {
		try {
			messagedigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成字符串的md5校验值
	 * 
	 * @param s
	 * @param charset 字符编码
	 * @return
	 */
	public static String getMD5String(String s, String charset) {
		String md5Result = "";
		try {
			md5Result = getMD5String(s.getBytes(charset));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Result;
	}

	/**
	 * 判断字符串的md5校验码是否与一个已知的md5码相匹配
	 * 
	 * @param password
	 *            要校验的字符串
	 * @param md5PwdStr
	 *            已知的md5校验码
	 * @return
	 */
	public static boolean checkPassword(String password, String md5PwdStr) {
		String s = getMD5String(password.getBytes());
		return s.equals(md5PwdStr);
	}


	/**
	 * JDK1.4中不支持以MappedByteBuffer类型为参数update方法，并且网上有讨论要慎用MappedByteBuffer，
	 * 原因是当使用 FileChannel.map 方法时，MappedByteBuffer 已经在系统内占用了一个句柄， 而使用
	 * FileChannel.close 方法是无法释放这个句柄的，且FileChannel有没有提供类似 unmap 的方法，
	 * 因此会出现无法删除文件的情况。 不推荐使用
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String getFileMD5String_old(File file) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
		messagedigest.update(byteBuffer);
		return bufferToHex(messagedigest.digest());
	}

	public static String getMD5String(byte[] bytes) {
		messagedigest.update(bytes);
		return bufferToHex(messagedigest.digest());
	}

	private static String bufferToHex(byte[] bytes) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte[] bytes, int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		// 取字节中高 4 位的数字转换, >>>
		// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		// 取字节中低 4 位的数字转换
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}


	private static String byteArrayToHexString(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (byte b : bytes) {
			int h = ((b >> 4) & 0x0f);
			if (h > 9){
				s.append((char) (h - 10 + 'a'));
			}else{
				s.append((char) (h + '0'));
			}

			h = (b & 0x0f);
			if (h > 9){
				s.append((char) (h - 10 + 'a'));
			} else{
				s.append((char) (h + '0'));
			}
		}

		return s.toString();
	}

	/**
	 *  加密
	 * @param plainText 明文
	 * @return 32位密文
	 */
	public static String encryption(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte[] b = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0){
					i += 256;
				}
				if (i < 16){
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			re_md5 = buf.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}
	
	/**
	 * Md5加密生成签名
	 * @author      WFZ
	 * @param
	 * @return
	 * @exception
	 * @date        2019/5/24 15:12
	 */
	public static String MD5Encode(String origin, String charsetName) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetName == null || "".equals(charsetName)){
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			}else{
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetName)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	
}
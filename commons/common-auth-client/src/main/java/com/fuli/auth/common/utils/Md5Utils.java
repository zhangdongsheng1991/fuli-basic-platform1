package com.fuli.auth.common.utils;

import java.security.MessageDigest;

/**
 * @Description: security 密码验证 MD5 +盐
 * @Author: FZ
 * @CreateDate: 2019/4/22 11:19
 * @Version: 1.0
 */
public class Md5Utils {

    /**
     * 默认用户密码
     */
    public static final String DEFAULT_PASSWORD = "111111";

    /**
     * MD5 盐
     */
    public static final String SALT = "xqc1254548787244";

    /**
     * MD5加盐加密
     *
     * @param password:密码
     * @return String
     * @author FZ
     * @date 2019/4/22 11:18
     */
    public static String encode(String password) {
        password = password + SALT;
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        char[] charArray = password.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++) {
            byteArray[i] = (byte) charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}

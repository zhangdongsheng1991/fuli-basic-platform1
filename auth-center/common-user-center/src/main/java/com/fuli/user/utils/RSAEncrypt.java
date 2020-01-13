package com.fuli.user.utils;

import com.fuli.user.constant.CommonConstant;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Description:    RSA 解密
 * @Author:         WFZ
 * @CreateDate:     2019/4/22 14:15
 * @Version:        1.0
*/
public class RSAEncrypt {

    /** 公钥*/
    public static final String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA3+uOOSPIfCszVdz6NPHhjgfSCNoPPUWmDcKWSW5yepiDw6Met7XKy2ETLbUCBcTTJXMqZNTh2I4b72oQgcF62JTzuV1faCJUQTM73vJUwxbXJKTGBIn2dZYHGG35whdgqL5dcL53oU7cUaXqXHCn5rfJVo7FHnC+ePEKAtSGzQIDAQAB";

    /**
     * 生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 得到公钥字符串（Base64加密）
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        //keyMap.put(0,publicKeyString);  //0表示公钥
        //keyMap.put(1,privateKeyString);  //1表示私钥
    }
    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey){
        try{
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte));
            return outStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取解密并截取后的密码
     * @author      WFZ
     * @param str : 加密字符串
     * @param privateKey : 私钥
     * @return 返回解密并截取后的字符串
     */
    public static String getPassword(String str, String privateKey){
        String password = decrypt(str, privateKey);
        if (StringUtils.isNotEmpty(password)){
            if(password.indexOf(CommonConstant.PASSWORD_SPLIT_SIGN) != -1){
                return password.substring(0, password.indexOf(CommonConstant.PASSWORD_SPLIT_SIGN));
            }
        }
        return "";
    }
}

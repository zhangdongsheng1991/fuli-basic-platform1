package com.fuli.cloud.commons.utils;

import com.fuli.cloud.constant.CommonConstant;
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
public class RsaEncryptUtil {

    /**
     * 公钥
     */
    public static final String PUBLIC_KEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCABmh0O3kyPI00im5RTpyOovClu56wqVWFVQTnWlvcLcaiYt7YREW73isotVUqGrJQ6PpNk7w9+VI0t11qg15Ng2TUmspEMXEz9hE5byinFjr5+dXyB3SQMonuyMYJJngHmYXf2qoOSsBBWpPVHNJP3G+Kic+I0Nzz+XCIXoCOyQIDAQAB";
    /**
     * 私钥
     */
    public static final String PRIVATE_KEY ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIAGaHQ7eTI8jTSKblFOnI6i8KW7nrCpVYVVBOdaW9wtxqJi3thERbveKyi1VSoaslDo+k2TvD35UjS3XWqDXk2DZNSaykQxcTP2ETlvKKcWOvn51fIHdJAyie7IxgkmeAeZhd/aqg5KwEFak9Uc0k/cb4qJz4jQ3PP5cIhegI7JAgMBAAECgYBOcx7IsrT2erJQUs981T4MDX6HICwCtxDnrgxeNDl79zTzxsAlJ9jPYTlzLYKtVUSVXd4bcX71PvUHxqDgpvU+jHq18SqeZu4bFPJN+ovZVL4l7RUiDdBGnISgiQ1EwHMOXowS1DMqGUtXnH9XvAxdp8VGPLHm8RsRxGNxjNVy7QJBAM7+9KACoGfJj4AZ5k6XA0G63R2gtpegot5hremU6G+sgvKf3YciLeVCjyLYLMu6mIfRQX/LZHC95wMh/VKNq5cCQQCeVWZtqRA0VoMVLv0daH4H6/le+JHttmjgmiy0V7pEx27kMcNeT3PYhnsl5aO32Fw6uZGdZb8wwEkmFWNp5GSfAkEAwzCJroUrixL3HV3O3dbq/EVgEnslExAn5LmJTFTdZcjv5ig3tiDV1HUSElJ001vOT2xUblR2DxYTy9F2Vi0/WwJAKT15Nmroq5k0/1mD2BsVaJ7QDyqTZ+UxH/9IUZjgDXYcrhdAGdp8BBPTE6XipMoYC7u179dGmz4aT28Wu8gfZQI/QIaj7n0OsHNycFQVKuafu2EA7kKKA2RdgjPLI/KfIFqXLiO1jRDMPLQtkT34JNcit8pDXxcDW51RvqVGnn/O";


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
        System.err.println(publicKeyString);
        System.err.println(privateKeyString);
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
     * @return 返回解密并截取后的字符串
     */
    public static String getPassword(String str){
        try{
            String password = decrypt(str, PRIVATE_KEY);
            if (StringUtils.isNotEmpty(password)){
                if(password.indexOf(CommonConstant.PASSWORD_SPLIT_SIGN) != -1){
                    return password.substring(0, password.indexOf(CommonConstant.PASSWORD_SPLIT_SIGN));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

//    public static void main(String[] args) {
//        try {
//            System.out.println(encrypt("cc209a064762d672c7cc2ef10c45372f##&&&20190729"));
//        }catch (Exception e){
//
//        }

//        System.out.println(getPassword("YaZoyJA8uTieFwYPmMfUN91BMfdR1Qxo0QBoMYEJ4qBSbUOHfpx/gmGlS2UtvQPQ0vCMol7J2caiCDhcpHDhXjdtiaVsStOC7vsfjY40yi8Iq66e/b7RxfU6l389pA78yUg/sZPANoNyUahz/OHaudv8CeuSlysfKwKdgNPs6yc"
//        ));
//    }
}

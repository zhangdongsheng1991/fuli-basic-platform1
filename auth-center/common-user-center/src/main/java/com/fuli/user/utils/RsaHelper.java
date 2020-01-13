package com.fuli.user.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 用于RSA非对称加密，服务端用私钥，客户端用公钥
 *
 * @author jimmy
 */
public class RsaHelper {

    /**
     * 非对称加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] encryptByKey(byte[] data, Key key) throws Exception {
        // 对数据加密
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 非对称解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public byte[] decryptByKey(byte[] data, Key key) throws Exception {
        // 对数据解密
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    /**
     * 非对称加密，并且将加密结果base64编码
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public String encryptWithBase64(String value, Key key) throws Exception {
        return Base64.encodeBase64String(encryptByKey(value.getBytes(), key));
    }

    /**
     * 对base64编码的数据进行非对称解密
     *
     * @param value
     * @param key
     * @return
     * @throws Exception
     */
    public String decryptWithBase64(String value, Key key) throws Exception {
        return new String(decryptByKey(Base64.decodeBase64(value.getBytes()), key));
    }

    /**
     * 获取公钥
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public PublicKey getPublicKey(String filename) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(getKey(filename));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    /**
     * 获取私钥
     *
     * @param filename
     * @return
     * @throws Exception
     */
    public PrivateKey getPrivateKey(String filename) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(getKey(filename));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private byte[] getKey(String filename) throws Exception {
        InputStream in = null;
        DataInputStream dis = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(filename);
            dis = new DataInputStream(in);
            byte[] keyBytes = new byte[in.available()];
            dis.readFully(keyBytes);
            return keyBytes;
        } catch (Exception e) {
            throw e;
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    /**
     * 从字符串获取私钥
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空", e);
        }
    }

    /**
     * 从字符串获取公钥
     *
     * @param publicKeyStr
     * @return
     * @throws Exception
     */
    public PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法", e);
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法", e);
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空", e);
        }
    }

}

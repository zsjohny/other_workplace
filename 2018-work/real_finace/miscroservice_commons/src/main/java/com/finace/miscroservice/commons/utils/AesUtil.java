package com.finace.miscroservice.commons.utils;


import com.finace.miscroservice.commons.log.Log;


import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * AES对称加密和解密
 */
public class AesUtil {
    public static String CIPHER_ALGORITHM = "AES"; // optional value AES/DES/DESede


    private static Key getKey(String strKey) throws NoSuchAlgorithmException {

        if (strKey == null) {
            strKey = "";
        }
        KeyGenerator _generator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(strKey.getBytes());
        _generator.init(128, secureRandom);
        return _generator.generateKey();

    }

    /**
     * 加密
     *
     * @param data 需要加密的数据
     * @param key  需要加密的密钥
     * @return
     */
    public static String encode(String data, String key) {
        String result = "";
        try {
            SecureRandom sr = new SecureRandom();
            Key secureKey = getKey(key);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);
            byte[] bt = cipher.doFinal(data.getBytes());
            result = Base64.getEncoder().encodeToString(bt);
        } catch (Exception e) {
            log.error("系统AES加密 data={} key={}  出错", data, key, e);

        }

        return result;
    }

    /**
     * 解密
     *
     * @param data 需要解密的数据
     * @param key  需要解密的密钥
     * @return
     */

    public static String decode(String data, String key) {
        String result = "";
        try {
            SecureRandom sr = new SecureRandom();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            Key secureKey = getKey(key);
            cipher.init(Cipher.DECRYPT_MODE, secureKey, sr);
            byte[] res =Base64.getDecoder().decode(data);

            result = new String(cipher.doFinal(res));
        } catch (Exception e) {
            log.error("系统AES解密 data={} key={}  出错", data, key, e);
        }
        return result;
    }

    private static Log log = Log.getInstance(AesUtil.class);

}
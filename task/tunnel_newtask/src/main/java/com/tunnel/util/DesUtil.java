package com.tunnel.util;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Base64;

public class DesUtil {

    private final static String DES = "DES";

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) {
        try {
            byte[] bt = encrypt(data.getBytes(), key.getBytes());
            String strs = Base64.getEncoder().encodeToString(bt);
            return strs;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) {
        try {
            if (data == null)
                return null;

            byte[] buf = Base64.getDecoder().decode(data);
            byte[] bt = decrypt(buf, key.getBytes());
            return new String(bt);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

    public static void main(String[] args) {

//        System.out.println(Base64.getEncoder().encodeToString("879227577".getBytes()));
//        System.exit(-1);
//        String[] dateArr = new String(Base64.getDecoder().decode("111")).split(":");
        String key = Base64.getEncoder().encodeToString("879227577".getBytes());
        String[] dateArr = new String(key+":2017:12:24").split(":");
        LocalDate date = LocalDate.now();
        String year = String.valueOf(DesUtil.encrypt(dateArr[1], new String(Base64.getDecoder().decode(dateArr[0]))));
        String month = String.valueOf(DesUtil.encrypt(dateArr[2], new String(Base64.getDecoder().decode(dateArr[0]))));
        String day = String.valueOf(DesUtil.encrypt(dateArr[3], new String(Base64.getDecoder().decode(dateArr[0]))));
        String value = key + ":" + year + ":" + month + ":" + day;
        System.out.println(Base64.getEncoder().encodeToString(value.getBytes()));


    }

}
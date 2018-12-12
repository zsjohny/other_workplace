package com.reliable.util;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesIosAndAndroid {
    private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".toCharArray();

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    public static String encode(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        int end = len - 3;
        int i = start;
        int n = 0;

        while (i <= end) {
            int d = (data[i] & 0xFF) << 16 | (data[(i + 1)] & 0xFF) << 8 | data[(i + 2)] & 0xFF;

            buf.append(legalChars[(d >> 18 & 0x3F)]);
            buf.append(legalChars[(d >> 12 & 0x3F)]);
            buf.append(legalChars[(d >> 6 & 0x3F)]);
            buf.append(legalChars[(d & 0x3F)]);

            i += 3;

            if (n++ >= 14) {
                n = 0;
                buf.append(" ");
            }
        }

        if (i == start + len - 2) {
            int d = (data[i] & 0xFF) << 16 | (data[(i + 1)] & 0xFF) << 8;

            buf.append(legalChars[(d >> 18 & 0x3F)]);
            buf.append(legalChars[(d >> 12 & 0x3F)]);
            buf.append(legalChars[(d >> 6 & 0x3F)]);
            buf.append("=");
        } else if (i == start + len - 1) {
            int d = (data[i] & 0xFF) << 16;

            buf.append(legalChars[(d >> 18 & 0x3F)]);
            buf.append(legalChars[(d >> 12 & 0x3F)]);
            buf.append("==");
        }

        return buf.toString();
    }

    public static String parseByte2HexStr(byte[] buf) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] decode(String s) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decode(s, bos);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
        }
        return decodedBytes;
    }

    private static void decode(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();
        while (true) {
            if ((i < len) && (s.charAt(i) <= ' ')) {
                i++;
            } else {
                if (i == len) {
                    break;
                }
                int tri = (decode(s.charAt(i)) << 18) + (decode(s.charAt(i + 1)) << 12) + (decode(s.charAt(i + 2)) << 6) + decode(s.charAt(i + 3));

                os.write(tri >> 16 & 0xFF);
                if (s.charAt(i + 2) == '=')
                    break;
                os.write(tri >> 8 & 0xFF);
                if (s.charAt(i + 3) == '=')
                    break;
                os.write(tri & 0xFF);

                i += 4;
            }
        }
    }

    private static int decode(char c) {
        if ((c >= 'A') && (c <= 'Z'))
            return c - 'A';
        if ((c >= 'a') && (c <= 'z'))
            return c - 'a' + 26;
        if ((c >= '0') && (c <= '9')) {
            return c - '0' + 26 + 26;
        }
        switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
        }
        throw new RuntimeException("unexpected code: " + c);
    }

    public static String encryptDES(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(encryptKey.substring(0, 8).getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(1, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encode(encryptedData);
        } catch (Exception e) {
        }
        return encryptString;
    }

    public static String decryptDES(String decryptString, String decryptKey) {
        try {
            byte[] byteMi = Base64.decode(decryptString);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(decryptKey.substring(0, 8).getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(2, key, zeroIv);
            byte[] decryptedData = cipher.doFinal(byteMi);

            return new String(decryptedData);
        } catch (Exception e) {
        }
        return decryptString;
    }

    public static void main(String[] args) {
        String ciphertext = encryptDES("111111", "135265575555");
        System.out.println("密文：" + ciphertext);
        System.out.println("解密后：" + decryptDES("o+Bh9jJTLBGdBUYz78RGBMesjgb0tze5", "18275793875"));
    }
}
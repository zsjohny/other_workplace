package com.jiuyuan.util;

import org.apache.commons.codec.digest.DigestUtils;

import com.yujj.util.StringUtil;


public class SignatureUtil {

    /**
     * 生成签名
     */
    public static String generateSignature(String source, byte[] key, int length) {
        byte[] bytes = saltByXOR(StringUtil.getBytes(source, "UTF-8"), key);
        String signature = DigestUtils.md5Hex(bytes);
        if (signature.length() > length) {
            signature = signature.substring(0, length);
        } else if (signature.length() != length) {
            throw new IllegalStateException("Failed to generate signature for: " + source + ", key: " +
                StringUtil.newString(key, "ISO-8859-1") + ", signature: " + signature);
        }
        return signature;
    }

    private static byte[] saltByXOR(byte[] bsrc, byte[] key) {
        int keyLen = key.length;
        for (int i = 0; i < bsrc.length; ++i) {
            bsrc[i] = (byte) (bsrc[i] ^ key[(i % keyLen)]);
        }
        return bsrc;
    }

}

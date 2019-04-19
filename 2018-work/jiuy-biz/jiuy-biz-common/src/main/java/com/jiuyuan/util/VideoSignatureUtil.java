package com.jiuyuan.util;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;

public class VideoSignatureUtil {
    private static final String HMAC_ALGORITHM = "HmacSHA1";
    private static final String HMAC_ALGORITHM256 = "HmacSHA256";
    private static final String CONTENT_CHARSET = "UTF-8";
    
    public String m_strSecId;
    public String m_strSecKey;
    public long m_qwNowTime;
    public int m_iRandom;
    public int m_iSignValidDuration;

    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public String getUploadSignature() {
        String strSign = "";
        String contextStr = "";
        long endTime = (m_qwNowTime + m_iSignValidDuration);
        try {
            contextStr += "secretId=" + java.net.URLEncoder.encode(this.m_strSecId, "utf8");
            contextStr += "&currentTimeStamp=" + this.m_qwNowTime;
            contextStr += "&expireTime=" + endTime;
            contextStr += "&random=" + this.m_iRandom;

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(m_strSecKey.getBytes(CONTENT_CHARSET), mac.getAlgorithm());
            mac.init(secretKey);
            byte[] hash = mac.doFinal(contextStr.getBytes(CONTENT_CHARSET));
            byte[] sigBuf = byteMerger(hash, contextStr.getBytes("utf8"));
            strSign = new String(new BASE64Encoder().encode(sigBuf).getBytes());
            strSign = strSign.replace(" ", "").replace("\n", "").replace("\r", "");
        } catch (Exception e) {
            System.out.print(e.toString());
            return "";
        }
        return strSign;
    }
    
    /**
     * 获取删除的签名
     * @param nonce 
     * @param timestamp 
     * @return
     */    
    public String getDeleteSignatureG(long fileId,int priority) {
    	String m_strSecId = "AKIDtb6MN980dz5uTl6X5MN8cqnTt3xb99OO";
        String m_strSecKey = "tYx72iAMJ0RfdMp2ye4E9pFQM3VhjW3J";
        long timestamp = System.currentTimeMillis()/1000;
    	int nonce = (int) ((Math.random()*9+1)*10000);
        String contextStr = "";
        String contextStr1 = "GETvod.api.qcloud.com/v2/index.php?Action=DeleteVodFile";
        String url = "";
        try {
            contextStr += "&fileId=" + java.net.URLEncoder.encode(""+fileId, "utf-8");
            contextStr += "&priority=" + java.net.URLEncoder.encode(""+priority, "utf-8");
            contextStr += "&SecretId=" + java.net.URLEncoder.encode(m_strSecId, "utf-8");
            contextStr += "&Region=" + java.net.URLEncoder.encode("sh", "utf-8");
            contextStr += "&Timestamp=" + java.net.URLEncoder.encode(""+timestamp, "utf-8");
            contextStr += "&Nonce=" + java.net.URLEncoder.encode(""+nonce, "utf-8");
            contextStr1 += "&Nonce=" + nonce;
        	System.out.println(nonce);
        	contextStr1 += "&Region=" + "sh";
        	contextStr1 += "&SecretId=" + m_strSecId;
        	contextStr1 += "&Timestamp=" + timestamp;
        	System.out.println(timestamp);
        	contextStr1 += "&fileId=" + fileId;
        	contextStr1 += "&priority=" + priority;
        	Mac sha256_HMAC = Mac.getInstance("HmacSHA1");
            SecretKeySpec secret_key = new SecretKeySpec(m_strSecKey.getBytes(), "HmacSHA1");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeBase64String(sha256_HMAC.doFinal((contextStr1).getBytes()));
            
            url = "https://vod.api.qcloud.com/v2/index.php?Action=DeleteVodFile"+contextStr+"&Signature="+java.net.URLEncoder.encode(hash, "utf-8");//java.net.URLEncoder.encode(hash, "utf-8");                
            System.out.println(url);
            return url;
        } catch (Exception e) {
			// TODO: handle exception
		}
        return "";
    }
    
}
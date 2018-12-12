package com.finace.miscroservice.commons.utils.Headline;

import com.finace.miscroservice.commons.utils.tools.BASE64Encoder;
import com.github.pagehelper.util.StringUtil;

import java.net.URLEncoder;
import java.security.Key;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HeadTools {

    /**
     *
     */
	public final static String HEAD_KEY = "7792e283-eac3-4a00-9995-0827847c4a24";

	/**
     * HMAC-SHA1签名
     *
     * @param message
     * @param key
     * @return
     */
    public static String getHmacSHA1(String message, String key) {
        String hmacSha1 = null;
        try {
            // url encode
            message = URLEncoder.encode(message, "UTF-8");
            // hmac-sha1加密
            Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(spec);
            byte[] byteHMAC = mac.doFinal(message.getBytes());
            // base64 encode
            hmacSha1 = new BASE64Encoder().encode(byteHMAC);
        } catch (Exception e) {
            //throw new ServiceException(e.getMessage());
        	e.printStackTrace();
        }
        return hmacSha1;
    }

    private static final String IOS10_INVALID_IDFA = "00000000-0000-0000-0000-000000000000";
    private static final String IOS9_INVALID_IDFA = "00000000000000000000000000000000";
    /**
     * 判断idfa是否合法
     *
     * @param idfa
     * @return
     */
    public static boolean checkIdfa(String idfa) {
        if (StringUtil.isEmpty(idfa) || IOS9_INVALID_IDFA.equals(idfa) || IOS10_INVALID_IDFA.equals(idfa)) {
            return false;
        }
        return true;
    }

}

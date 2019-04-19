package com.jfinal.weixin.jiuy.util;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
//import org.tuckey.web.filters.urlrewrite.utils.URLDecoder;

//import com.yujj.util.StringUtil;


public class EncodeUtil {

    /**
     * 如果容器使用ISO-8859-1对url进行解码，那么在服务端需要按ISO-8859-1编码还原为字节数组，再使用正确的字符集重新解码。
     * 
     * @param text 文本
     * @param charsetUsedToDecode 重新解码使用的字符集
     * @return 重新编码后的文本
     */
    public static String reEncode(String text, String charsetUsedToDecode) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        byte[] bytes = StringUtil.getBytes(text, "ISO-8859-1");
        return StringUtil.newString(bytes, charsetUsedToDecode);
    }
    /**
     *  需对url 做URL编码
     * @param url
     * @return
     */
    public static String encodeURL(String url ) {
    	return encodeURL(url,"UTF-8");
    }
    /**
     *  需对url 做URL编码
     * @param url
     * @return
     */
    public static String encodeURL(String url, String charset) {
        try {
            return URLEncoder.encode(url, charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     *  需对url 做URL解码
     * @param url
     * @return
     */
    public static String decodeURL(String url ) {
    	return decodeURL(url,"UTF-8");
    }
    
    /**
     *  需对url 做URL解码
     * @param url
     * @return
     */
    public static String decodeURL(String url, String charset) {
			try {
				return URLDecoder.decode(url, charset);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
    }
    public static String encodeBase64String(String string) {
        return encodeBase64String(string,"UTF-8");
    }
    public static String encodeBase64String(String string, String charset) {
        byte[] bytes = StringUtil.getBytes(string, charset);
        return Base64.encodeBase64String(bytes);
    }

    public static String encodeBase64URLSafeString(String string, String charset) {
        byte[] bytes = StringUtil.getBytes(string, charset);
        return Base64.encodeBase64URLSafeString(bytes);
    }

    public static String decodeBase64(String base64String, String charset) {
        return StringUtil.newString(Base64.decodeBase64(base64String), charset);
    }
    public static String decodeBase64(String base64String ) {
        return decodeBase64(base64String,"UTF-8");
    }
}

package com.jfinal.third.util.weixinpay;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeixinPayCore {
    private static final Logger logger = LoggerFactory.getLogger(WeixinPayCore.class);

    /**
     * 生成签名
     */
    public static String genSign(List<NameValuePair> params, String apiKey) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(apiKey);

        String packageSign = DigestUtils.md5Hex(sb.toString()).toUpperCase();
        return packageSign;
    }

    public static String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        return sb.toString();
    }

    public static Map<String, String> decodeXml(String content) {
        Map<String, String> xml = new HashMap<String, String>();
        try {
            Document document = DocumentHelper.parseText(content);
            for (@SuppressWarnings("unchecked")
            Iterator<Element> it = document.getRootElement().elementIterator(); it.hasNext();) {
                Element element = it.next();
                xml.put(element.getName(), element.getText());
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return xml;
    }

    public static String genNonceStr() {
        Random random = new Random();
        return DigestUtils.md5Hex(String.valueOf(random.nextInt(10000)));
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}

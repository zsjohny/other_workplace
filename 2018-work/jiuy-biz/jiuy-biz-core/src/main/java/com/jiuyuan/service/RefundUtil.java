package com.jiuyuan.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.core.io.ClassPathResource;
import org.xml.sax.InputSource;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * TODO:后续改掉
 */
public class RefundUtil {
    public static void main(String[] args) {

//        reqData.put("transaction_id", "4200000178201809079393725152");
//        reqData.put("out_trade_no", "S100006433_07164734");
//        reqData.put("refund_fee", "1");
//        reqData.put("refund_desc", "重新下单");
//        reqData.put("out_refund_no", "50001040");
//        reqData.put("total_fee", "1");


        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", "wx6ad169bccc57554a");//应用id
        packageParams.put("mch_id", "1403320302");//商户号
        packageParams.put("nonce_str", System.currentTimeMillis() + "");//随机字符串
        packageParams.put("out_trade_no", "S100006433_07164734");//订单号
        packageParams.put("out_refund_no", "S100006433_07164734");//退款单号
        packageParams.put("total_fee", "1");//订单总金额Utils.getMoney()
        packageParams.put("refund_fee", "1");//退款总金额
        packageParams.put("op_user_id", "1403320302");//商户号

        String sign = signMd5_2(packageParams, "ebdd1da629156627139d0b5be22bee67");
        String result = "FAIL";
        String msg = "";
        System.out.println("--sign--=" + sign);

        String createOrderURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
        String xml = null;
        try {
            xml = createXML(packageParams, sign.toUpperCase());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("--xml-=" + xml);
        String retur = null;
        try {
            retur = doRefund(createOrderURL, xml);

            System.out.print(retur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(retur)) {
            Map map = parseXmlToMap(retur);

            String returnCode = (String) map.get("return_code");
            if (returnCode.equals("SUCCESS")) {
                result = "SUCCESS";
                msg = "OK";
                int status = -1;
                String resultCode = (String) map.get("result_code");
                if (resultCode.equals("SUCCESS")) {
                    status = 1;
                }
                if (status == 1) {
                    String outtradeno = (String) map.get("out_trade_no"); // 订单号
                }
            }


        }

    }

    private static Map parseXmlToMap(String xml) {
        //  Map retMap = new HashMap();
        SortedMap<String, String> retMap = new TreeMap<String, String>();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc = sb.build(source);
            Element root = doc.getRootElement();// 指向根节点
            List<Element> es = root.getChildren();
            if (es != null && es.size() != 0) {
                for (Element element : es) {
                    retMap.put(element.getName(), element.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retMap;
    }


    private static String doRefund(String url, String data) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream is = new FileInputStream(new ClassPathResource("apiclient_cert.p12").getFile());
        char[] passArray = "1403320302".toCharArray();
        try {
            keyStore.load(is, passArray);// 这里写密码..默认是你的MCHID
        } finally {
            is.close();
        }

        // Trust own CA and all self-signed certs
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, passArray)// 这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[]{"TLSv1"}, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }


    private static String createXML(Map<String, String> map, String s) throws UnsupportedEncodingException {
        StringBuilder xml = new StringBuilder("<xml>");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            xml.append("<").append(key).append(">").append(val).append("</").append(key).append(">");
        }
        xml.append("<").append("sign").append(">").append(s).append("</").append("sign").append(">");
        xml.append("</xml>");
        String xml1 = xml.toString();
        return xml1;
    }

    private static String signMd5_2(Map<String, String> packageParams, String merchantKey) {
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + merchantKey);
        String sign = Md5(sb.toString());
        return sign;
    }

    private static String Md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {

                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {

        }
        return result.toUpperCase();
    }

}
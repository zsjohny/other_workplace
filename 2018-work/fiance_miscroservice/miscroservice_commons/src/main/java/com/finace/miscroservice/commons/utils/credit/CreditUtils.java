package com.finace.miscroservice.commons.utils.credit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.finace.miscroservice.commons.log.Log;
//import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.util.*;

//import static org.junit.Assert.assertEquals;

@Component
@Configuration
public class CreditUtils {

    private static Log logger = Log.getInstance(CreditUtils.class);

    //版本号
    private static final String VERSION = "10";
    //交易渠道
    private static final String COINSTCHANNEL = "000001";

    //接口调用（说明：平台需要根据实际访问环境进行修改，本测试地址直接连接即信端SIT环境）
    public static String URI;
    //页面调用
    public static String PAGE_URI;
    //银行代码
    private static String BANKCODE;
    //机构代码   （说明：平台需要根据即信端给定实际参数进行调整）
    private static String INSTCODE;

    @Value("${creditgo.bankcode}")
    public void setBANKCODE(String BANKCODE) {
        CreditUtils.BANKCODE = BANKCODE;
    }

    @Value("${creditgo.instcode}")
    public void setINSTCODE(String INSTCODE) {
        CreditUtils.INSTCODE = INSTCODE;
    }

    @Value("${creditgo.online.url}")
    public void setURI(String URI) {
        CreditUtils.URI = URI;
    }

    @Value("${creditgo.url}")
    public void setPageUri(String pageUri) {
        PAGE_URI = pageUri;
    }


    /**
     * 通用部分统一管理
     *
     * @param reqMap
     * @return
     */
    public Map<String, String> getHeadReq(Map<String, String> reqMap) {

        reqMap.put("version", VERSION);
        reqMap.put("instCode", INSTCODE);
        reqMap.put("bankCode", BANKCODE);
        reqMap.put("txDate", DateUtil.getDate());
        reqMap.put("txTime", DateUtil.getTime());
        reqMap.put("seqNo", DateUtil.getRandomStr(6));
        reqMap.put("channel", COINSTCHANNEL);
        return reqMap;
    }

    /**
     * 组织参数发起请求
     *
     * @param reqMap
     * @throws Exception
     */
    @SuppressWarnings({"serial", "rawtypes", "unchecked"})
    public Map creditGo(Map<String, String> reqMap) throws Exception {

        //生成待签名字符串
        String requestMapMerged = mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);

        reqMap.put("sign", sign);

        RestTemplate restTemplate = new RestTemplate(new ArrayList<HttpMessageConverter<?>>() {{
            add(new FastJsonHttpMessageConverter());
        }});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");
        headers.set("contentType", "application/json");
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        HttpEntity entity = new HttpEntity(reqMap, headers);
        logger.info("请求银行端流水号：" + reqMap.get("txDate") + reqMap.get("txTime") + reqMap.get("seqNo"));
        logger.info("(P2P-->即信端)请求信息：\r\n" + JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));

        logger.info("\r\n(P2P-->即信端)发送请求至：" + URI + "\r\n");

        //请求到即信端
        ResponseEntity response = restTemplate.exchange(URI, HttpMethod.POST, entity, Map.class);

        //        //响应报文
        logger.info("(即信端-->P2P)响应报文：\r\n" + response.getBody().toString().replace(",", ",\r\n"));
        Map responseMap = (Map) response.getBody();

        //验签
        String responseSign = (String) responseMap.get("sign");
        responseMap.remove("sign");

        String responseMapMerged = mergeMap(new TreeMap(responseMap));

        boolean verifyResult = SignUtil.verify(responseSign, responseMapMerged.toString());
        if (!verifyResult) {
            logger.info("(P2P-->即信端)验证签名失败...");
            return null;
        } else {
            logger.info("(P2P-->即信端)验证签名成功");
            return responseMap;
        }
    }


    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            logger.info("Warning: URL Host: " + urlHostName + " vs. "
                    + session.getPeerHost());
            return true;
        }
    };

    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
                .getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc
                .getSocketFactory());
    }

    static class miTM implements javax.net.ssl.TrustManager,
            javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(
                java.security.cert.X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }

    }

    /**
     * 获取Map的待签名字符串
     *
     * @param map
     * @return
     */
    public static String mergeMap(Map<String, String> map) {
        //字典序排序后生成待签名字符串
        Map<String, String> reqMap = new TreeMap<String, String>(map);

        StringBuffer buff = new StringBuffer();

        Iterator<Map.Entry<String, String>> iter = reqMap.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            if (!"sign".equals(entry.getKey())) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                    buff.append("");
                } else {
                    buff.append(String.valueOf(entry.getValue()));
                }
            }
        }

        String requestMerged = buff.toString();
        return requestMerged;
    }



    public Map sendRequest(String url,Map<String, String> reqMap) throws Exception{

        //生成待签名字符串
        String requestMapMerged = mergeMap(reqMap);
        //生成签名
        String sign = SignUtil.sign(requestMapMerged);

        reqMap.put("sign", sign);

        RestTemplate restTemplate = new RestTemplate(new ArrayList<HttpMessageConverter<?>>(){{add(new FastJsonHttpMessageConverter());}});
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept-Charset", "UTF-8");
        headers.set("contentType","application/json");
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);

        HttpEntity entity = new HttpEntity(reqMap,headers);
        logger.info("请求银行端流水号："+reqMap.get("txDate")+reqMap.get("txTime")+reqMap.get("seqNo"));
        logger.info("(P2P-->即信端)请求信息：\r\n"+JSON.toJSON(reqMap).toString().replace(",", ",\r\n"));
        logger.info("\r\n(P2P-->即信端)发送请求至："+URI+"\r\n");

        //请求到即信端
        ResponseEntity response  = restTemplate.exchange(URI,HttpMethod.POST, entity, Map.class);

        //响应报文
        logger.info("(即信端-->P2P)响应报文：\r\n"+response.getBody().toString().replace(",", ",\r\n"));
        Map responseMap = (Map)response.getBody();

        //验签
        String responseSign = (String) responseMap.get("sign");
        responseMap.remove("sign");

        String responseMapMerged = mergeMap(new TreeMap(responseMap));

        boolean verifyResult = SignUtil.verify(responseSign, responseMapMerged.toString());
        if (!verifyResult){
            logger.info("(P2P-->即信端)验证签名失败...");
        } else {
            logger.info("(P2P-->即信端)验证签名成功");
            return responseMap;
        }

        logger.info("retCode:" + responseMap.get("retCode"));
        logger.info("retMsg:"+responseMap.get("retMsg"));

//        assertEquals(verifyResult, true);
        return null;
    }
}

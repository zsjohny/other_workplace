//package com.finace.miscroservice.commons.utils.creditGo;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpStatus;
//import org.apache.http.StatusLine;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//
//import static com.finace.miscroservice.commons.utils.creditGo.Credit2go.mergeMap;
//
//@Component
//public class HttpUtils {
//    /**
//     * post请求（用于请求json格式的参数）
//     * @param url
//     * @return
//     */
//    @SuppressWarnings({ "serial", "rawtypes", "unchecked" })
//    public  String doPost(String url, Map<String, String> reqMap) throws Exception {
//        //生成待签名字符串
//        String requestMapMerged = mergeMap(reqMap);
//        //生成签名
//        String sign = SignUtil_lj.sign(requestMapMerged);
//
//        reqMap.put("sign", sign);
//        CloseableHttpClient httpclient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(url);// 创建httpPost
//        httpPost.setHeader("Accept", "application/json");
//        httpPost.setHeader("Content-Type", "application/json");
//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(reqMap.toString(), charSet);
//        httpPost.setEntity(entity);
//        CloseableHttpResponse response = null;
//
//        try {
//
//            response = httpclient.execute(httpPost);
//
////            Map responseMap = (Map)response.getBody();
////
////            //验签
////            String responseSign = (String) responseMap.get("sign");
////            responseMap.remove("sign");
//
//
//            StatusLine status = response.getStatusLine();
//            int state = status.getStatusCode();
//            if (state == HttpStatus.SC_OK) {
//                HttpEntity responseEntity = response.getEntity();
//                String jsonString = EntityUtils.toString(responseEntity);
//                return jsonString;
//            }
//            else{
////                logger.error("请求返回:"+state+"("+url+")");
//                System.out.println("请求返回:"+state+"("+url+")");
//            }
//        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }
//
//}

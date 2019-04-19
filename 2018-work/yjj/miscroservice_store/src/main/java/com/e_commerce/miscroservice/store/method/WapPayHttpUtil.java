package com.e_commerce.miscroservice.store.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.Map;


/**
 * http请求工具
 * @author Administrator
 *
 */
public class WapPayHttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(WapPayHttpUtil.class);
    public final static String GET = "GET";
    public final static String POST = "POST";
    
    public static String sendGetHttp(String requestUrl , String outputStr) {
   	 return sendHttp( requestUrl ,  GET ,  outputStr);
   }
    
    public static String sendPostHttp(String requestUrl , String outputStr) {
    	 return sendHttp( requestUrl ,  POST ,  outputStr);
    }
    
    public static  Map<String, Object> sendPostHttpReturnMap(String requestUrl , String outputStr) {
   	 	String ret = sendHttp( requestUrl ,  POST ,  outputStr);
   	 	
   	 	Map<String, Object> retMap = JsonStrToMap(ret);
   	 	return retMap ;
    }
    
    
    
    
	/** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 可以是xml或json
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static String sendHttp(String requestUrl , String requestMethod , String outputStr) {
    	String ret = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
            sslContext.init(null , tm , new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
//          jsonObject = JSONObject.fromObject(buffer.toString());
            ret =  buffer.toString();
        } catch (ConnectException ce) {
        	logger.info("请求超时",ce);
            System.out.println("请求超时");
        } catch (Exception e) {
            System.out.println("请求错误");
            logger.info("请求错误",e);
        }
//      return jsonObject;
        return ret;
    }
    
    
   
    /**
     * 发送get请求
     * @param url
     * @return
     */
	public static String sendGetHttp(String url) {
	    logger.info("WxRequestUtil.getAccessToken url:"+url); 
	    String ret = null;
	    try {  
	        URL urlGet = new URL(url); 
	        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();  
	        http.setRequestMethod("GET"); // 必须是get方式请求  
	        http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");  
	        http.setDoOutput(true);  
	        http.setDoInput(true);  
	        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
	        System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
	        http.connect();  
	        InputStream is = http.getInputStream();  
	        int size = is.available();  
	        byte[] jsonBytes = new byte[size];  
	        is.read(jsonBytes);  
	        ret = new String(jsonBytes, "UTF-8");  
	        is.close();  
	    } catch (Exception e) {  
	            e.printStackTrace();  
	    }  
	    
	    return ret;  
	}  
    
    
	public static Map<String, Object> JsonStrToMap(String jsonStr) {
	        Map<String, Object> map = JSON.parseObject(jsonStr,new TypeReference<Map<String, Object>>(){} );
	        return map;
	}
	    
	public static String getValueByKey(String jsonStr,String key) {
	        JSONObject jsonObj = JSONObject.parseObject(jsonStr);
	        String value = jsonObj.getString(key);  
	        return value;
	}
	public static String sendHttpSaveImgToAliYun(String requestUrl  , String outputStr) {
		   return sendHttpSaveImgToAliYun( requestUrl , GET ,  outputStr);
	}
	/** 
     * 发起https请求并获取结果 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 可以是xml或json
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值) 
     */  
    public static String sendHttpSaveImgToAliYun(String requestUrl , String requestMethod , String outputStr) {
    	String imgPath = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
            sslContext.init(null , tm , new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if("GET".equalsIgnoreCase(requestMethod)){
				httpUrlConn.connect();
			}

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            
            //保存图片到服务器
            imgPath = saveFile(inputStream);
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
//          jsonObject = JSONObject.fromObject(buffer.toString());
//            ret =  buffer.toString();
        } catch (ConnectException ce) {
        	logger.info("请求超时",ce);
            System.out.println("请求超时");
        } catch (Exception e) {
            System.out.println("请求错误");
            logger.info("请求错误",e);
        }
//      return jsonObject;
        return imgPath;
    }

	private static String saveFile(InputStream inputStream) throws FileNotFoundException {
		String fileDir=System.getProperty("java.io.tmpdir")+"/";
 		 String fileName = System.currentTimeMillis()+".jpg";
 		 String imgPath = fileDir + fileName;
 		 return imgPath;
	}
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	  public final static void main(String[] args) throws Exception {
//		 String ret =  clientCustomSSL();
	  }
	  public static String clientCustomSSL(String url, String xmlParameters, String mchId)throws Exception {
		  	String ret = "";
	  
	        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
	        FileInputStream instream = new FileInputStream(new File("F:/cert/apiclient_cert.p12"));
	        try {
	            keyStore.load(instream, mchId.toCharArray());
	        } finally {
	            instream.close();
	        }

	        // Trust own CA and all self-signed certs
	        SSLContext sslcontext = SSLContexts.custom()
	                .loadKeyMaterial(keyStore, mchId.toCharArray())
	                .build();
	        // Allow TLSv1 protocol only
	        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                sslcontext,
	                new String[] { "TLSv1" },
	                null,
	                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
	        CloseableHttpClient httpclient = HttpClients.custom()
	                .setSSLSocketFactory(sslsf)
	                .build();
	        try {

//	        	 HttpGet httpget = new HttpGet("https://api.mch.weixin.qq.com/secapi/pay/refund"); 
//	        	HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
	        	HttpPost httpPost = new HttpPost(url);
	        	httpPost.setEntity(new StringEntity(xmlParameters, Charset.forName("UTF-8")));

	            System.out.println("executing request" + httpPost.getRequestLine());

	            CloseableHttpResponse response = httpclient.execute(httpPost);
	            try {
	                HttpEntity entity = response.getEntity();

	                System.out.println("----------------------------------------");
	                System.out.println(response.getStatusLine());
	                if (entity != null) {
	                    System.out.println("Response content length: " + entity.getContentLength());
	                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
	                    String text;
	                    while ((text = bufferedReader.readLine()) != null) {
	                    	ret = ret + text;
	                        System.out.println("----"+text);
	                    }
	                }
	                EntityUtils.consume(entity);
	            } finally {
	                response.close();
	            }
	        } finally {
	            httpclient.close();
	        }
	        
	        return ret;
	        
	  }

	

}

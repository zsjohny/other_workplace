package com.jfinal.third.util.weixinpay;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jfinal.log.Log;
import com.jfinal.third.api.ImgErToFileUtil;
import com.jfinal.third.api.ThirdCodeApi;
import com.jfinal.weixin.sdk.utils.HttpUtils;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;



/**
 * http请求工具
 * @author Administrator
 *
 */
public class WapPayHttpUtil {
	static Log logger = Log.getLog(WapPayHttpUtil.class);
    public final static String GET = "GET";
    public final static String POST = "POST";

    public static void main(String[] args) {

      	Map<String, String> map = new HashMap<String, String>();
    		map.put("storeId", "208");
    		map.put("authorizer_appid", "wxfc022fadb7c14600");
    		map.put("authorizer_info_jsonstr", "{}");
//        	Response<String> resp = Requests.get(PropKit.get("wxa_api_url")+ApiManager.wxaAuthUrl).params(map).text();
//    	 	String body = resp.getBody();

    		String param = JSONObject.toJSONString(map);
    		String url = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=";
    		String url2 ="http://dev.yujiejie.com:8080/wxa/wxaAuth.do";
    		String url3 ="https://wxalocal.yujiejie.com/wxa/wxaAuth.do";

    		logger.info("开始获取授权小程序帐号的可选类目, url:"+url);
    	 	Response<String> resp = Requests.get(url3).text();
    	 	String body1 = resp.getBody();
    		System.out.println("body1============"+body1);
		Response<String> resp2 = Requests.get(url3).params(map).text();//""

	 	String body2 = resp2.getBody();
		System.out.println("body2============"+body2);

		String body4 =HttpUtils.get(url3,map);
	 	System.out.println("body4============"+body4);
		System.out.println("111111111111");
//		String body = WapPayHttpUtil.sendPostHttp(url2, param);
//		System.out.println("小程序授权body:"+body);
//	 	String body3 = WapPayHttpUtil.sendPostHttpReturnMap(url2, param).toString();
//		System.out.println("小程序授权body3:"+body3);
//


	}

    public static String sendGetHttp(String requestUrl , String outputStr) {
   	 return sendHttp( requestUrl ,  GET ,  outputStr);
   }

    public static String sendPostHttp(String requestUrl , String outputStr) {
    	 return sendHttp( requestUrl ,  POST ,  outputStr);
    }

    public static  Map<String, Object> sendPostHttpReturnMap(String requestUrl , String outputStr) {
   	 	String ret = sendHttp( requestUrl ,  POST ,  outputStr);
   	 	logger.info("post请求返回结果="+ret);
   	 	Map<String, Object> retMap = JsonStrToMap(ret);
   	 	return retMap ;
    }
    public static  Map<String, Object> sendGetHttpReturnMap(String requestUrl , String outputStr) {
   	 	String ret = sendHttp( requestUrl ,  GET ,  outputStr);

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
    	 System.out.println("发送请求，requestUrl："+requestUrl+",outputStr:"+outputStr);
         logger.info("发送请求，requestUrl："+requestUrl+",outputStr:"+outputStr);
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

            if ("GET".equalsIgnoreCase(requestMethod))httpUrlConn.connect();

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
            ce.printStackTrace();
        } catch (Exception e) {
            System.out.println("请求错误");
            logger.info("请求错误",e);
            e.printStackTrace();
        }
//      return jsonObject;
        return ret;
    }



//    public static String sendHttpImg(String requestUrl , String outputStr) {
//      	 return sendHttpImg( requestUrl ,  GET ,  outputStr);
//      }
//
//    /**
//     * 发起https请求并获取结果
//     *
//     * @param requestUrl 请求地址
//     * @param requestMethod 请求方式（GET、POST）
//     * @param outputStr 提交的数据 可以是xml或json
//     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
//     */
//    public static String sendHttpImg(String requestUrl , String requestMethod , String outputStr) {
//    	 System.out.println("发送请求，requestUrl："+requestUrl+",outputStr:"+outputStr);
//         logger.info("发送请求，requestUrl：",requestUrl+",outputStr:"+outputStr);
//    	String ret = null;
//        StringBuffer buffer = new StringBuffer();
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() };
//            SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
//            sslContext.init(null , tm , new java.security.SecureRandom());
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//
//            URL url = new URL(requestUrl);
//            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//            httpUrlConn.setSSLSocketFactory(ssf);
//
//            httpUrlConn.setDoOutput(true);
//            httpUrlConn.setDoInput(true);
//            httpUrlConn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            httpUrlConn.setRequestMethod(requestMethod);
//
//            if ("GET".equalsIgnoreCase(requestMethod))httpUrlConn.connect();
//
//            // 当有数据需要提交时
//            if (null != outputStr) {
//                OutputStream outputStream = httpUrlConn.getOutputStream();
//                // 注意编码格式，防止中文乱码
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//
//            // 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            String fileDir=System.getProperty("java.io.tmpdir")+"/";
////   		 String fileDir="D:/";
//   		 	String fileName = System.currentTimeMillis()+".jpg";
//   		 	String imgPath = fileDir + fileName;
//
////            InputStream   inputStream   =   new   ByteArrayInputStream(binarySystemImgStr.getBytes());
//			int saveImgRet = ImgErToFileUtil.saveToImgByInputStream(inputStream,fileDir,fileName);
//
//			logger.info("========================"  );
//			 logger.info("========================"  );
//			 logger.info("========================"  );
//			 logger.info("========================"  );
//			 logger.info("==sendHttpImg======saveImgRet:"+saveImgRet+",imgPath:" + imgPath );
//			 System.out.println("发送请求，sendHttpImg=====saveImgRet:"+saveImgRet+",imgPath:" + imgPath );
//			 logger.info("========================"  );
//			 logger.info("========================"  );
//			 logger.info("========================"  );
//			 logger.info("========================"  );
//
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream , "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//            // 释放资源
//            inputStream.close();
//            inputStream = null;
//            httpUrlConn.disconnect();
////          jsonObject = JSONObject.fromObject(buffer.toString());
//            ret =  buffer.toString();
//        } catch (ConnectException ce) {
//        	logger.info("请求超时",ce);
//            System.out.println("请求超时");
//            ce.printStackTrace();
//        } catch (Exception e) {
//            System.out.println("请求错误");
//            logger.info("请求错误",e);
//            e.printStackTrace();
//        }
////      return jsonObject;
//        return ret;
//    }





    public static  Map<String, Object> sendGetHttpReturnMap(String requestUrl) {
   	 	String ret = sendGetHttp( requestUrl   );

   	 	Map<String, Object> retMap = JsonStrToMap(ret);
   	 	return retMap ;
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




	public static String sendHttpSaveImgToAliYun(String requestUrl  , String outputStr,String realPath) {
//		   logger.info("==========将小程序商品二维码图片保存到服务器(1)realPath:{"+realPath+"}");
		   return sendHttpSaveImgToAliYun( requestUrl , GET ,  outputStr,realPath);
	}
	/**
  * 发起https请求并获取结果
  *
  * @param requestUrl 请求地址
  * @param requestMethod 请求方式（GET、POST）
  * @param outputStr 提交的数据 可以是xml或json
  * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
  */
 public static String sendHttpSaveImgToAliYun(String requestUrl , String requestMethod , String outputStr,String realPath) {
//	logger.info("==========将小程序商品二维码图片保存到服务器(2)realPath:{"+realPath+"}");
 	String imgPath = null;
     StringBuffer buffer = new StringBuffer();
     try {
         // 创建SSLContext对象，并使用我们指定的信任管理器初始化
         TrustManager[] tm = { new MyX509TrustManager() };
//         logger.info("==========将小程序商品二维码图片保存到服务器(3)tm:{"+tm+"}");
         SSLContext sslContext = SSLContext.getInstance("SSL" , "SunJSSE");
//         logger.info("==========将小程序商品二维码图片保存到服务器(4)sslContext:{"+sslContext+"}");
         sslContext.init(null , tm , new java.security.SecureRandom());
         // 从上述SSLContext对象中得到SSLSocketFactory对象
         SSLSocketFactory ssf = sslContext.getSocketFactory();
//         logger.info("==========将小程序商品二维码图片保存到服务器(5)ssf:{"+ssf+"}");

         URL url = new URL(requestUrl);
         HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//         logger.info("==========将小程序商品二维码图片保存到服务器(6)httpUrlConn:{"+httpUrlConn+"}");
         httpUrlConn.setSSLSocketFactory(ssf);

         httpUrlConn.setDoOutput(true);
         httpUrlConn.setDoInput(true);
         httpUrlConn.setUseCaches(false);
         // 设置请求方式（GET/POST）
         httpUrlConn.setRequestMethod(requestMethod);
//         logger.info("==========将小程序商品二维码图片保存到服务器(7)flag:{"+"GET".equalsIgnoreCase(requestMethod)+"}");
         if ("GET".equalsIgnoreCase(requestMethod))httpUrlConn.connect();

         // 当有数据需要提交时
         if (null != outputStr) {
             OutputStream outputStream = httpUrlConn.getOutputStream();
             logger.info("==========将小程序商品二维码图片保存到服务器(8)outputStream:{"+outputStream+"}");
             // 注意编码格式，防止中文乱码
             outputStream.write(outputStr.getBytes("UTF-8"));
             outputStream.close();
         }

         // 将返回的输入流转换成字符串
         InputStream inputStream = httpUrlConn.getInputStream();
//         logger.info("==========将小程序商品二维码图片保存到服务器(9)inputStream:{"+inputStream+"}");

         //将返回的输入流备份
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] bufferArr = new byte[1024];
         int len;
         while ((len = inputStream.read(bufferArr)) > -1 ) {
             baos.write(bufferArr, 0, len);
         }
         baos.flush();
         logger.info("==========将小程序商品二维码图片保存到服务器(10)将返回的输入流备份");
         InputStream inputStreamFirst = new ByteArrayInputStream(baos.toByteArray());

         InputStream inputStreamSecond = new ByteArrayInputStream(baos.toByteArray());

         //判断是否有错误
         BufferedReader in = new BufferedReader(new InputStreamReader(inputStreamFirst, "utf-8"));
         String readLine = in.readLine();
         if(readLine.startsWith("{\"errcode")){
        	 return "";
         }
//         logger.info("==========将小程序商品二维码图片保存到服务器(11)readLine:{"+readLine+"}");
//         logger.info("==========将小程序商品二维码图片保存到服务器(12)判断是否有错误:{"+readLine.startsWith("{\"errcode"));
         //如果没有错误
         //保存图片到服务器
         if(StringUtils.isEmpty(realPath)){
        	 logger.info("==========将小程序商品二维码图片保存到服务器(13)第一步realPath:{"+realPath+"}");
        	 imgPath = saveFile(inputStreamSecond);
         }else{
        	 logger.info("==========将小程序商品二维码图片保存到服务器(14)第二步realPath:{"+realPath+"}");
        	 imgPath = saveFile(inputStreamSecond,realPath);
         }

         // 释放资源
         inputStream.close();
         inputStream = null;
         httpUrlConn.disconnect();
//       jsonObject = JSONObject.fromObject(buffer.toString());
//         ret =  buffer.toString();
     } catch (ConnectException ce) {
     	logger.info("请求超时",ce);
         System.out.println("请求超时");
     } catch (Exception e) {
         System.out.println("请求错误");
         logger.info("请求错误",e);
     }
//   return jsonObject;
     return imgPath;
 }

 private static String saveFile(InputStream inputStream) throws FileNotFoundException {
		String fileDir=System.getProperty("java.io.tmpdir")+"/";
		 String fileName = System.currentTimeMillis()+".jpg";
		 String imgPath = fileDir + fileName;
		 int saveImgRet = ImgErToFileUtil.saveToImgByInputStream(inputStream,fileDir,fileName);
		 return imgPath;
	}
 private static String saveFile(InputStream inputStream,String realPath) throws FileNotFoundException {
		String fileDir=realPath+"/image/";
	 	logger.info("==========将小程序商品二维码图片保存到服务器()fileDir:{"+fileDir+"}");
		Calendar rightNow = Calendar.getInstance();
        Integer year = rightNow.get(Calendar.YEAR);
        Integer month = rightNow.get(Calendar.MONTH)+1; //第一个月从0开始，所以得到月份＋1
        Integer day = rightNow.get(rightNow.DAY_OF_MONTH);
        fileDir = fileDir + year + "/" + month + "/" + day + "/";
        File file = new File(fileDir);
        if (!file .exists()) {
        	logger.info("获取小程序二维码:创建文件夹:file:"+file.getPath());
        	file.mkdirs();
        }
        logger.info("获取小程序二维码：获取当前年月日：year:"+year+";month:"+month+";day:"+day);
		 String fileName = System.currentTimeMillis()+".jpg";
		 String imgPath = fileDir + fileName;

		 logger.info("==========获取小程序二维码fileName:{"+fileName+"};imgPath:"+imgPath);
		 int saveImgRet = ImgErToFileUtil.saveToImgByInputStream(inputStream,fileDir,fileName);
		 return "/image/" + year + "/" + month + "/" + day + "/" + fileName;
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


}

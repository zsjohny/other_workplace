package com.jiuyuan.util;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.entity.store.StoreWxa;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title
 * @package jiuy-biz
 * @description
 * @date 2018/6/21 14:57
 * @copyright 玖远网络
 */
public class HttpClientUtils {


    private static Logger m_logger = Logger.getLogger(HttpClientUtils.class);


    public static String get(String url, Map<String, Object> param) {
        StringBuilder builder = new StringBuilder(url);
        builder.append("?");

        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            builder.append(key).append("=").append(String.valueOf(value)).append("&");
        }
        return get(builder.toString().substring(0, builder.length() - 1));
    }


    public static String get(String url) {
        m_logger.info("get请求 url===>" + url);
        String responseBody = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    m_logger.error("Unexpected response status: " + status);
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };

            responseBody = httpclient.execute(httpGet, responseHandler);
        } catch (Exception e) {
            m_logger.error("httpclient get error!" + e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                m_logger.error("close httpclient error!" + e);
            }
        }

        m_logger.error("responseBody:" + responseBody);
        return responseBody;
    }


    public static String post(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            //设置tcp连接的存活时间
            conn.setReadTimeout(2000);
            conn.setConnectTimeout(2000);

            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            m_logger.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static AtomicInteger index = new AtomicInteger(1);

    private static ExecutorService executorService = Executors.newFixedThreadPool(20, (ThreadFactory) r -> {
        Thread thread = new Thread(r);
        thread.setName(HttpClientUtils.class.getCanonicalName() + index.getAndIncrement());
        return thread;
    });

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }));
    }

    public static File postInputStreamToFile(String url, String param, String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
            m_logger.info("删除缓存文件 param="+param+",fileName="+fileName);
        }

        File dir = file.getParentFile();
        if (! dir.exists()) {
            m_logger.info("创建父级目录");
            dir.mkdirs();
        }

        Future<File> submit = executorService.submit(() -> postToFile(url, param, fileName));


        try {
            file = submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return file;


    }


    public static void main(String[] args) throws IOException {
//
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                for (int j = 0; j < 20; j++) {

        String url = "http://weixinonline.yujiejie.com//third/findAccessTokenByAppId";
        Map<String, Object> param = new HashMap<>(2);
        StoreWxa storeWxa = null;
        param.put("appId", "wx3c6998d9c0837a61");
        String token = HttpClientUtils.get(url, param);

        Map<String, String> paramMap = new HashMap<String, String>();
//                    paramMap.put("scene", "storeId=" + 12928 + "&type=1");
        paramMap.put("scene", "21129");
        paramMap.put("page", "pages/component/detail/detail");
        paramMap.put("width", "430");

        long timeMillis = System.nanoTime();
        HttpClientUtils.postInputStreamToFile(String.format("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s"
                , token), JSONObject.toJSONString(paramMap), "C:\\Users\\nessa\\Downloads\\" + timeMillis + ".jpg");

//                }
//            }).start();
//        }
    }


    public static File postToFile(String url, String param, String fileName) {

        File targetFile = null;
        PrintWriter out = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        FileOutputStream downloadFile = null;
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            inputStream = conn.getInputStream();
            targetFile = new File(fileName);
            StringBuilder content = new StringBuilder();
            int index;
            byte[] bytes = new byte[1024];
            downloadFile = new FileOutputStream(targetFile);
            while ((index = inputStream.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                content.append(new String(bytes, 0, index));
                downloadFile.flush();
            }
            if (content.length() < 200) {

                System.out.println("结果" + content.toString());
            }


        } catch (Exception e) {
            m_logger.error("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }

            }

            if (downloadFile != null) {
                try {
                    downloadFile.close();
                } catch (IOException e) {

                }

            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }
            if (conn != null) {
                m_logger.info("断开连接");
                conn.disconnect();
            }
        }
        return targetFile;


    }
}


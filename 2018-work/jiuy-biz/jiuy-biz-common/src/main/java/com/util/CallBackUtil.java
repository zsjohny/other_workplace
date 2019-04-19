package com.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对外提供的回调工具类
 * @author Think
 */
public class CallBackUtil {

    private CallBackUtil() {

    }

    private static final String BASE_URL = "https://%s.yujiejie.com/task_scheduling/call";
    private static final String POST = "POST";

    private static String LOCAL = "local";
    private static String ONLINE = "online";
    //IP线上名单
    private static String ONLINE_IP_V4 = "47.96.153.80,47.98.113.138";
    private static String PROFILE = LOCAL;


    static {
        String CONTAIN_INFO = "fk=\"";

        Pattern compile = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");

        String internetIp = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new
                URL("http://www.baidu.com/s?wd=ip").openStream()))) {

            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(CONTAIN_INFO)) {
                    Matcher matcher = compile.matcher(line);
                    while (matcher.find()) {
                        internetIp = matcher.group();
                        break;
                    }

                }
            }

        } catch (Exception e) {

        }

        if (StringUtils.isNotBlank(internetIp) && ONLINE_IP_V4.contains(internetIp)) {
            PROFILE = ONLINE;
        }

    }


    /**
     * 发送回调处理
     *
     * @param url    回调请求的相对地址 eg user/xxx 前面没有/
     * @param params 回调请求的参数 可以为空,不为空时,必须是json的字符串形式
     * @param method 回调请求的方式 get和post
     */
    public static void send(String params, String url, String method) {
        HttpURLConnection conn = null;
        try {

            conn = (HttpURLConnection) new URL(String.format(BASE_URL, PROFILE))
                    .openConnection();
            conn.setRequestMethod(POST);
            conn.setConnectTimeout(3000);
            conn.setDoOutput(Boolean.TRUE);
            conn.setDoInput(Boolean.TRUE);
            OutputStream outputStream = conn.getOutputStream();
            StringBuilder builder = new StringBuilder();
            builder.append("url=");
            builder.append(url);
            if (!(params == null || params.isEmpty())) {
                builder.append("&params=");
                builder.append(params);
            }
            builder.append("&method=");
            builder.append(method);

            Map<String, String> threeMap = new TreeMap<>();
            threeMap.put("url", url);
            threeMap.put("params", params);
            threeMap.put("method", method);

            StringBuilder signBuild = new StringBuilder();
            for (Map.Entry<String, String> entry : threeMap.entrySet()) {
                signBuild.append(entry.getKey());
                signBuild.append("=");
                signBuild.append(entry.getValue());
            }
            builder.append("&sign=");
            builder.append(md5(signBuild.toString()));
            outputStream.write(builder.toString().getBytes());
            conn.connect();
            conn.getInputStream();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    private static String md5(String sourceStr) {
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

            e.printStackTrace();
        }
        return result.toUpperCase();

    }

    public static void main(String[] args) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("send_type", 17);
//        jsonObject.put("phone", "18888888888");
//        jsonObject.put("verify_code", "1234");
//        send("jstore/store/login/verifyCommitV372.do",
//                jsonObject.toJSONString(), "get");

        System.out.println(ONLINE_IP_V4.contains("47.98.113.138"));
    }


}

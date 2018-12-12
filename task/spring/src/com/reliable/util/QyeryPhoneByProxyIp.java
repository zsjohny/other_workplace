package com.reliable.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class QyeryPhoneByProxyIp {
    public static String httpURLConnectionGET(String GET_URL, String param) {
        String msg = "";
        try {
            URL url = new URL(new StringBuilder().append(GET_URL).append("?").append(param).toString());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            connection.disconnect();
            msg = sb.toString();
        } catch (Exception e) {
        }

        return msg;
    }

    public static Map<String, String> httpURLConnectionPOST(String POST_URL, String param, QueryPhoneSetAttr queryPhoneSetAttr, String readPropertiesName) {
        Map map = new HashMap();
        try {
            URL url = new URL(POST_URL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            QueryPhoneSetAttr.setSetConnecAttr(connection, readPropertiesName);

            connection.setDoOutput(true);

            connection.setDoInput(true);

            connection.setRequestMethod("POST");

            connection.setUseCaches(false);

            connection.setInstanceFollowRedirects(true);

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            out.write(param);

            out.flush();
            out.close();

            connection.connect();

            if (connection.getResponseCode() >= 400) {
                InputStream is = connection.getErrorStream();

                StringBuilder sbBuilder = new StringBuilder();
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = is.read(buff, 0, buff.length)) != -1) {
                    sbBuilder.append(new String(buff, 0, len));
                }

                is.close();

                map.put("status", String.valueOf(connection.getResponseCode()));

                map.put("content", sbBuilder.toString());
                return map;
            }

            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = "";
            StringBuilder sb = new StringBuilder();

            while ((line = bf.readLine()) != null) {
                sb.append(line);
            }

            bf.close();
            connection.disconnect();

            map.put("status", String.valueOf(connection.getResponseCode()));

            map.put("content", sb.toString());
        } catch (Exception e) {
        }
        return map;
    }

    private static void commonProxyIp() {
        try {
            Properties properties = new Properties();

            String webUrl = new StringBuilder().append(System.getProperty("user.dir")).append("\\WebRoot\\properties\\proxyIp.properties").toString();
            properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(new File(webUrl)), "utf-8")));

            int readNum = (int) (Math.random() * (properties.size() / 2)) + 1;

            System.getProperties().setProperty("http.proxyHost", properties.getProperty(new StringBuilder().append("http.proxyHost").append(readNum).append("").toString()).trim());

            System.getProperties().setProperty("http.proxyPort", properties.getProperty(new StringBuilder().append("http.proxyPort").append(readNum).append("").toString()).trim());
        } catch (Exception e) {
        }
    }
}
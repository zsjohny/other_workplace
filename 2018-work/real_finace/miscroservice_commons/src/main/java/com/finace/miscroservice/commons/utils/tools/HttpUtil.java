package com.finace.miscroservice.commons.utils.tools;

import com.finace.miscroservice.commons.log.Log;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {

    private static int connectTimeOut = 20000;
    private static int readTimeOut = 20000;

    private static Log logger = Log.getInstance(HttpUtil.class);

    /**
     * http get方式发送消息
     *
     * @param reqUrl       请求url地址
     * @param parameters
     * @param recvEncoding
     * @return
     */
    public static String doGet(String reqUrl, Map<String, String> parameters,
                               String recvEncoding) {
        HttpURLConnection url_con;
        String responseContent;
        url_con = null;
        responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator<Entry<String, String>> iter = parameters.entrySet()
                    .iterator(); iter.hasNext(); params.append("&")) {
                Entry<String, String> element = (Entry<String, String>) iter
                        .next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        "utf-8"));
            }

            if (params.length() > 0)
                params = params.deleteCharAt(params.length() - 1);
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(connectTimeOut);
            url_con.setReadTimeout(readTimeOut);
            url_con.setDoOutput(true);
            byte b[] = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    "utf-8"));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            for (; tempLine != null; tempLine = rd.readLine()) {
                temp.append(tempLine);
                temp.append(crlf);
            }
            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url_con != null)
            url_con.disconnect();
        return responseContent;
    }

    public static InputStream doGetContract(String reqUrl, Map<String, String> parameters,
                               String recvEncoding) {
        HttpURLConnection url_con;
        String responseContent;
        url_con = null;
        responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator<Entry<String, String>> iter = parameters.entrySet()
                    .iterator(); iter.hasNext(); params.append("&")) {
                Entry<String, String> element = (Entry<String, String>) iter
                        .next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        "utf-8"));
            }

            if (params.length() > 0)
                params = params.deleteCharAt(params.length() - 1);
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(connectTimeOut);
            url_con.setReadTimeout(readTimeOut);
            url_con.setDoOutput(true);
            byte b[] = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    "utf-8"));
           /* String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            for (; tempLine != null; tempLine = rd.readLine()) {
                temp.append(tempLine);
                temp.append(crlf);
            }
            responseContent = temp.toString();
            rd.close();
            in.close();*/
            return  in;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (url_con != null)
            url_con.disconnect();
        return null;
    }

    /**
     * http get方式发送消息
     *
     * @param reqUrl
     * @param recvEncoding
     * @return
     */
    public static String doGet(String reqUrl, String recvEncoding) {
        HttpURLConnection url_con;
        String responseContent;
        url_con = null;
        responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");
            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl
                        .length());
                String paramArray[] = parameters.split("&");
                for (int i = 0; i < paramArray.length; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string
                                .length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value, recvEncoding));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            url_con.setRequestMethod("GET");
            url_con.setConnectTimeout(connectTimeOut);
            url_con.setReadTimeout(readTimeOut);
            url_con.setDoOutput(true);
            byte b[] = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer temp = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            for (; tempLine != null; tempLine = rd.readLine()) {
                temp.append(tempLine);
                temp.append(crlf);
            }

            responseContent = temp.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("发送失败", e);
        }
        if (url_con != null)
            url_con.disconnect();
        return responseContent;
    }

    /**
     * http post方式发送消息
     *
     * @param reqUrl
     * @param parameters
     * @param recvEncoding
     * @return
     */
    public static String doPost(String reqUrl, Map<String, String> parameters,
                                String recvEncoding) {
        logger.info("parameters={}",parameters);
        HttpURLConnection url_con;
        String responseContent;
        url_con = null;
        responseContent = null;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator<Entry<String, String>> iter = parameters.entrySet()
                    .iterator(); iter.hasNext(); params.append("&")) {
                Entry<String, String> element = (Entry<String, String>) iter
                        .next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(),
                        recvEncoding));
            }

            if (params.length() > 0)
                params = params.deleteCharAt(params.length() - 1);
            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(connectTimeOut);
            url_con.setReadTimeout(readTimeOut);
            url_con.setDoOutput(true);
            byte b[] = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(in,
                    recvEncoding));
            String tempLine = rd.readLine();
            StringBuffer tempStr = new StringBuffer();
            String crlf = System.getProperty("line.separator");
            for (; tempLine != null; tempLine = rd.readLine()) {
                tempStr.append(tempLine);
                tempStr.append(crlf);
            }

            responseContent = tempStr.toString();
            rd.close();
            in.close();
        } catch (IOException e) {
            logger.error("发送失败！", e);
        }
        return responseContent;
    }

    public static void put(HttpSession session, String key, Object value) {
        session.setAttribute(key, value);
    }

    public static Object get(HttpSession session, String key) {
        return session.getAttribute(key);
    }

    public static void remove(HttpSession session, String key) {
        session.removeAttribute(key);
    }

    public static void clear(HttpSession session) {
        session.invalidate();
    }

    // Cookie op.
    public static String get(HttpServletRequest req, String key) {
        Cookie[] cookies = req.getCookies();
        if (null == cookies)
            return null;
        for (Cookie c : cookies)
            if (c.getName().equals(key))
                return c.getValue();
        return null;
    }

    public static void put(HttpServletResponse resp, String[] keys, String[] values) {
        CookieGenerator g = new CookieGenerator();
        g.setCookiePath("/");
        g.setCookieMaxAge(86400);// 一天时间
        for (int i = 0; i < keys.length; i++) {
            g.setCookieName(keys[i]);
            g.addCookie(resp, values[i]);
        }
    }

    public static void put(HttpServletResponse resp, String[] domains, String[] keys, String[] values) {
        CookieGenerator g = new CookieGenerator();
        g.setCookiePath("/");
        g.setCookieMaxAge(86400);// 一天时间
        for (String domain : domains) {
            g.setCookieDomain(domain);
            for (int i = 0; i < keys.length; i++) {
                g.setCookieName(keys[i]);
                g.addCookie(resp, values[i]);
            }
        }
    }

    public static void remove(HttpServletResponse resp, String[] keys) {
        disableCookie(resp);
        /*
        CookieGenerator g = new CookieGenerator();
		g.setCookiePath("/");
		
		g.setCookieMaxAge(86400);// 一天时间
		for (int i = 0; i < keys.length; i++) {
			g.setCookieName(keys[i]);
			g.removeCookie(resp);
		}
		*/
    }

    public static void remove(HttpServletResponse resp, String[] domains, String[] keys) {
        disableCookie(resp);
		/*
		CookieGenerator g = new CookieGenerator();
		g.setCookiePath("/");
		
		for (String domain : domains) {
			g.setCookieDomain(domain);
			for (String key : keys) {
				g.setCookieName(key);
				g.removeCookie(resp);
			}
		}
		*/
    }

    public static void disableCookie(HttpServletResponse resp) {
        CookieGenerator g = new CookieGenerator();
        g.setCookiePath("/");

        //HttpContext.SessionKey.LOGINING_ACTIVE.toString()
        g.setCookieName("active");
        g.addCookie(resp, "1");
    }
}

package com.jiuyuan.util.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiuyuan.util.enumeration.StringEnum;
import com.yujj.ext.servlet.pre3.ServerCookie;

public class CookieUtil {

    private static final Logger log = LoggerFactory.getLogger(CookieUtil.class);

    /**
     * <pre>
     * 如果cookie含有@等特殊字符，在tomcat6下通过getCookie无法获取完整的值。此时需要从cookie header分析。
     * 
     * 注意：如果一个Cookie header包含多个cookie（以半角逗号分隔），这个方法可能会有问题。
     * </pre>
     */
    public static String getNonSpecCookieValue(String name, HttpServletRequest request) {
        try {
            if (name == null)
                return "";

            String cookies = request.getHeader("Cookie");
            if (cookies != null) {
                name = name + "=";
                int fromIndex = cookies.indexOf(name);
                if (fromIndex >= 0) {
                    int endIndex = cookies.indexOf(";", fromIndex);
                    if (endIndex >= 0) {
                        return cookies.substring(fromIndex + name.length(), endIndex);
                    } else {
                        return cookies.substring(fromIndex + name.length());
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    public static String getNonSpecCookieValue(StringEnum name, HttpServletRequest request) {
        return getNonSpecCookieValue(name.getStringValue(), request);
    }

    public static String getCookieValue(HttpServletRequest request, String cookieName, String defaultValue) {
        Cookie cookieList[] = request.getCookies();
        if (cookieList == null || cookieName == null)
            return defaultValue;
        for (int i = 0; i < cookieList.length; i++) {
            try {
                if (cookieList[i].getName().equals(cookieName))
                    return cookieList[i].getValue();
            } catch (Exception e) {
                log.error("Cookie", e);
            }
        }
        return defaultValue;
    }

    public static String getCookieValue(HttpServletRequest request, StringEnum cookieName, String defaultValue) {
        return getCookieValue(request, cookieName.getStringValue(), defaultValue);
    }

    public static String buildCookieHeaderValue(Cookie cookie, boolean isHttpOnly) {
        return ServerCookie.buildCookieHeaderValue(cookie, isHttpOnly);
    }

    public static String buildCookieHeaderValue(String name, String value, String path, String domain, Integer maxAge,
                                                boolean isHttpOnly) {
        int intMaxAge = maxAge != null ? maxAge : -1;
        return ServerCookie.buildCookieHeaderValue(0, name, value, path, domain, null, intMaxAge, false, isHttpOnly);
    }

    public static String buildCookieHeaderValue(StringEnum name, String value, String path, String domain,
                                                Integer maxAge, boolean isHttpOnly) {
        return buildCookieHeaderValue(name.getStringValue(), value, path, domain, maxAge, isHttpOnly);
    }

    public static void deleteCookie(HttpServletResponse response, String cookieName, String domain, String path) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletResponse response, StringEnum cookieName, String domain, String path) {
        deleteCookie(response, cookieName.getStringValue(), domain, path);
    }

    public static String formatDateForCookie(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss z", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(date);
    }

    public static void main(String[] args) {
        String pattern = "EEE MMM dd HH:mm:ss Z yyyy";
    }
}

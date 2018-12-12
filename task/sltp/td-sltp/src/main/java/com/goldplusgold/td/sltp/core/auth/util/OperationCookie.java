package com.goldplusgold.td.sltp.core.auth.util;

import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;


/**
 * Cookie操作
 */
public class OperationCookie {


    /**
     * 返回要删除的cookie
     */
    public static Cookie getDelCookie(String cookieName, String domain) {
        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setDomain(domain);
        cookie.setPath("/");
        return cookie;
    }


    /**
     * 返回新创建的cookie
     */
    public static Cookie getNewCookie(String cookieName,
                                      String cookieValue,
                                      Integer maxAge,
                                      String domain) {

        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        return cookie;
    }


    /**
     * 返回更新后cookie
     */
    public static Cookie getUpdatedCookie(Cookie cookie,
                                          Integer maxAge,
                                          String domain) {
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        return cookie;
    }


    /**
     * 根据cookieName获取cookieValue
     */
    public static String getCookieValue(HttpServletRequest req,
                                        String cookieName) {

        String cookieValue = null;
        Optional<Cookie> optCookie = Optional.empty();
        if (ArrayUtils.isNotEmpty(req.getCookies())) {
            optCookie = Arrays.stream(req.getCookies()).
                    filter(cookie -> cookieName.equals(cookie.getName())).findFirst();
        }
        if (optCookie.isPresent()) {
            cookieValue = optCookie.get().getValue();
        }
        return cookieValue;
    }
}

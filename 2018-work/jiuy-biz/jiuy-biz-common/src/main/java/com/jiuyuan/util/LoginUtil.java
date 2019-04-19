package com.jiuyuan.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.constant.CookieConstants;
import com.jiuyuan.constant.account.UserType;
import com.jiuyuan.util.http.CookieUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class LoginUtil {

    private static final String COOKIE_ENCRYPT_KEY = "=W!xB^&0U5AkNr]FQz{S?B+OU[OAa90J";

    private static final String COOKIE_PART_SPLIT = "#";

    public static void deleteLoginCookie(HttpServletResponse response) {
        CookieUtil.deleteCookie(response, CookieConstants.COOKIE_NAME_SESSION, CookieConstants.COOKIE_DOMAIN, "/");
    }

    public static String buildLoginCookieHeaderValue(String value) {
        return CookieUtil.buildCookieHeaderValue(CookieConstants.COOKIE_NAME_SESSION, value, "/",
                CookieConstants.COOKIE_DOMAIN, null, true);
    }

    public static String buildLoginCookieValue(String userRelatedName, UserType userType) {
        long time = System.currentTimeMillis();
        String souce = userRelatedName + COOKIE_PART_SPLIT + userType.getIntValue() + COOKIE_PART_SPLIT + time;
        return AESUtil.encrypt(souce, "UTF-8", COOKIE_ENCRYPT_KEY);
    }

    private static final Map<String, String> cookieMap = new ConcurrentSkipListMap<>();

    public static String[] getLoginCookieParts(HttpServletRequest request) {

        String deviceToken = request.getHeader("user-agent");
        System.out.printf("token:"+deviceToken);
        if(cookieMap.get(deviceToken)!=null){
            System.out.printf("has:"+cookieMap.get(deviceToken));
            return StringUtils.split(cookieMap.get(deviceToken), COOKIE_PART_SPLIT);
        }else {
            String cookieValue = CookieUtil.getCookieValue(request, CookieConstants.COOKIE_NAME_SESSION, null);
            System.out.printf("no:"+cookieValue);
            if (cookieValue == null) {
                cookieValue = (String) request.getSession().getAttribute(CookieConstants.COOKIE_NAME_SESSION);
                if (cookieValue == null)
                    return null;
            }

            String result = AESUtil.decrypt(cookieValue, "UTF-8", COOKIE_ENCRYPT_KEY);
            if (StringUtils.isBlank(result)) {
                return null;
            }
            cookieMap.put(deviceToken,result);
            return StringUtils.split(result, COOKIE_PART_SPLIT);
        }
//        String cookieValue = CookieUtil.getCookieValue(request, CookieConstants.COOKIE_NAME_SESSION, null);
//        if (cookieValue == null) {
//			cookieValue = (String) request.getSession().getAttribute(CookieConstants.COOKIE_NAME_SESSION);
//			if (cookieValue == null)
//				return null;
//        }
//
//        String result = AESUtil.decrypt(cookieValue, "UTF-8", COOKIE_ENCRYPT_KEY);
//        if (StringUtils.isBlank(result)) {
//            return null;
//        }
//        return StringUtils.split(result, COOKIE_PART_SPLIT);
    }

}

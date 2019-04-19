package com.jiuyuan.web.interceptor;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.Platform;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.ExtractedDate;
import com.jiuyuan.util.http.HttpUtil;

public class SetRequestAttributesInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SetRequestAttributesInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Map<String, String> req = new HashMap<String, String>();
        request.setAttribute("_req", req);
        req.put("baseUrl", HttpUtil.getBaseUrl(request));
        req.put("url", HttpUtil.getRequestUrl(request));
        req.put("fullUrl", HttpUtil.getFullRequestUrl(request));
        req.put("hostName", HttpUtil.getHostName());
        req.put("clientIp", HttpUtil.getClientIp(request));
        req.put("referer", HttpUtil.getReferer(request));
        req.put("method", StringUtils.lowerCase(request.getMethod()));
        req.put("ua", StringUtils.defaultString(request.getHeader("User-Agent")));

        request.setAttribute("envRuntime", Constants.ENV_RUNTIME);

        request.setAttribute("serverUrl", Constants.SERVER_URL);
        request.setAttribute("serverUrlHttps", Constants.SERVER_URL_HTTPS);

        Calendar calendar = Calendar.getInstance();
        request.setAttribute("_now", new ExtractedDate(calendar));
        request.setAttribute("now", System.currentTimeMillis());
        
        setUserAgentPlatform(request);
        setClientPlatform(request);

        return true;
    }

    private void setUserAgentPlatform(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        Platform platform = Platform.DESKTOP;
        if (StringUtils.containsIgnoreCase(userAgent, "windows phone")) {
            platform = Platform.WINDOWS_PHONE;
        } else if (StringUtils.containsIgnoreCase(userAgent, "iphone")) {
            platform = Platform.IPHONE;
        } else if (StringUtils.containsIgnoreCase(userAgent, "ipod")) {
            platform = Platform.IPOD;
        } else if (StringUtils.containsIgnoreCase(userAgent, "ipad")) {
            platform = Platform.IPAD;
        } else if (StringUtils.containsIgnoreCase(userAgent, "android")) {
            if (StringUtils.containsIgnoreCase(userAgent, "tablet")) {
                platform = Platform.ANDROID_PAD;
            } else {
                platform = Platform.ANDROID;
            }
        }
        request.setAttribute(Constants.KEY_USER_AGENT_PLATFORM, platform);
    }

    private void setClientPlatform(HttpServletRequest request) {
        Platform platform = Platform.DESKTOP;
        String version = "";
        try {
            String xUserAgent = request.getHeader("X-User-Agent");
            if(StringUtils.isNotBlank(xUserAgent)) {
                String[] parts = StringUtils.split(xUserAgent, "|");
                String platformStr = StringUtils.split(parts[0], "=")[1];
                version = StringUtils.split(parts[1], "=")[1];
                if (StringUtils.equalsIgnoreCase(platformStr, "android")) {
                    platform = Platform.ANDROID;
                } else if (StringUtils.equalsIgnoreCase(platformStr, "iphone")) {
                    platform = Platform.IPHONE;
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        request.setAttribute(Constants.KEY_CLIENT_PLATFORM, new ClientPlatform(platform, version));
    }
}

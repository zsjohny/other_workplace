package com.jiuyuan.web.interceptor;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;

import com.jiuyuan.util.http.HttpUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.web.helper.JsonAllowed;

public class ContentTypeFilterInterceptor extends HandlerInterceptorAdapter {

    public static final Pattern CALLBACK_PATTERN = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*");

    private static final Logger logger = LoggerFactory.getLogger(ContentTypeFilterInterceptor.class);

    @Autowired
    private UrlPathHelper urlPathHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        if (HttpUtil.isJsonRequest(request)) {
            Method method = handlerMethod.getMethod();
            String requestUri = urlPathHelper.getRequestUri(request);
            String lowerRequestUri = requestUri.toLowerCase();
            return filterJson(response, method, lowerRequestUri);
        }

        return true;
    }

    private boolean filterJson(HttpServletResponse response, Method method, String requestUri) throws Exception {
        boolean jsonAllowed =
            AnnotationUtils.findAnnotation(method, JsonAllowed.class) != null ||
                JsonResponse.class.isAssignableFrom(method.getReturnType());

        if (!jsonAllowed) {
            String msg = "Json not allowed for this request: " + requestUri;
            logger.warn(msg);
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}

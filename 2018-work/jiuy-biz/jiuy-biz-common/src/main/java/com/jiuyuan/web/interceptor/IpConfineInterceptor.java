package com.jiuyuan.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.util.http.HttpUtil;
import com.yujj.util.StringUtil;
import com.yujj.web.helper.IpConfine;

public class IpConfineInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;

        IpConfine ipConfine = AnnotationUtils.findAnnotation(method.getMethod(), IpConfine.class);
        if(ipConfine == null){
            ipConfine = AnnotationUtils.findAnnotation(method.getBeanType(), IpConfine.class);
        }

        if (ipConfine == null) {
            return true;
        }

        String[] ips = ipConfine.value();
        String ip = HttpUtil.getClientIp(request);
        boolean pass = ipConfine.blackMode() ? !StringUtil.in(ip, ips) : StringUtil.in(ip, ips);
        if (!pass) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        return pass;
    }
}

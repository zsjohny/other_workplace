package com.store.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.UserDetail;


public class MDCInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDetail userDetail = (UserDetail) request.getAttribute(Constants.KEY_USER_DETAIL);
        if(userDetail != null){
	        if (userDetail.getId() > 0) {
	            MDC.put("userId", "" + userDetail.getId());
	        }
        }else{
        	System.out.println("userDetail is null");
        }

        return true;
    }
}

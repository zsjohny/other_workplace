package com.yujj.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.yujj.entity.account.UserDetail;

public class MDCInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserDetail userDetail = (UserDetail) request.getAttribute(Constants.KEY_USER_DETAIL);
        if(userDetail != null){
	        if (userDetail.getUserId() > 0) {
	            MDC.put("userId", "" + userDetail.getUserId());
	        }
        }else{
        	System.out.println("userDetail is null");
        }

        return true;
    }
}

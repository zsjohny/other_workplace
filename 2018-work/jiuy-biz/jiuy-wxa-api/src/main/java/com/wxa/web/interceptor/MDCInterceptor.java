package com.wxa.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.constant.Constants;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
//import com.store.entity.UserDetail;
import com.store.entity.member.UserDetail;


public class MDCInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	 ShopDetail shopDetail = (ShopDetail) request.getAttribute(Constants.KEY_WXA_SHOP_DETAIL);
    	 MemberDetail memberDetail = (MemberDetail) request.getAttribute(Constants.KEY_WXA_MEMBER_DETAIL);
         if(shopDetail != null){
	        if (shopDetail.getId() > 0) {
	            MDC.put("shopDetail", "" + shopDetail.getId());
	        }
        }else{
        	System.out.println("shopDetail is null");
        }
         if(memberDetail != null){
 	        if (memberDetail.getId() > 0) {
 	            MDC.put("memberDetail", "" + memberDetail.getId());
 	        }
         }else{
         	System.out.println("memberDetail is null");
         }

        return true;
    }
}

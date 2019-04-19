package com.wxa.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.SecurityService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.util.http.HttpUtil;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.web.help.JsonpResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;

/**
 * 登录检查拦截器
 * 
 * @see Login
 * @see NoLogin
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HandlerMethod method = (HandlerMethod) handler;

        boolean noLoginRequired = AnnotationUtils.findAnnotation(method.getMethod(), NoLogin.class) != null;
        if (noLoginRequired) {
            return true;
        }

        boolean loginRequired =
            AnnotationUtils.findAnnotation(method.getBeanType(), Login.class) != null ||
                AnnotationUtils.findAnnotation(method.getMethod(), Login.class) != null;
        if (!loginRequired) {
            return true;
        }

//        UserDetail userDetail = (UserDetail) request.getAttribute(Constants.KEY_USER_DETAIL);
        
        ShopDetail shopDetail = (ShopDetail) request.getAttribute(Constants.KEY_WXA_SHOP_DETAIL);
        MemberDetail memberDetail = (MemberDetail) request.getAttribute(Constants.KEY_WXA_MEMBER_DETAIL);
        
        if (shopDetail.getId() > 0 && memberDetail.getId() > 0) {
            return true;
        }else{
        	 JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
             jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
             String text = objectMapper.writeValueAsString(jsonpResponse);
             HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
        }
        
//
//        if (HttpUtil.isJsonRequest(request)) {
//            JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
//            jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//
//            String text = objectMapper.writeValueAsString(jsonpResponse);
//            HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
//        } else {
//        	
//        	
//            UriBuilder builder = new UriBuilder(Constants.SERVER_URL_HTTPS + "/login.do");
//            if (StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
//                builder.add("target_url", HttpUtil.getFullRequestUrl(request));
//            } else {
//                String loginRedirectUrl = request.getParameter("login_redirect_url");
//                if (StringUtils.isNotBlank(loginRedirectUrl)) {
//                    loginRedirectUrl = securityService.getSafeRedirectUrl(loginRedirectUrl);
//                    if (StringUtils.startsWith(loginRedirectUrl, "/")) {
//                        loginRedirectUrl = Constants.SERVER_URL + loginRedirectUrl;
//                    }
//                    builder.add("target_url", loginRedirectUrl);
//                }
//            }
//            response.sendRedirect(builder.toUri());
//        }

        return false;
    }
}

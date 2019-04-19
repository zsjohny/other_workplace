package com.ground.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.GroundUser;
import com.jiuyuan.service.SecurityService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.NoLogin;
import com.jiuyuan.util.http.HttpUtil;
import com.jiuyuan.util.http.MimeTypes;
import com.jiuyuan.web.help.JsonpResponse;
import com.yujj.util.uri.UriBuilder;


/**
 * 登录检查拦截器
 * 
 * @see Login
 * @see NoLogin
 */
public class CheckLoginInterceptor extends HandlerInterceptorAdapter {
	 private static final Logger logger = LoggerFactory.getLogger(CheckLoginInterceptor.class);
	 
	 private static final int ORIGINAL_PASSWORD = 1;
 	
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityService securityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	 // logger.info("登录检查拦截器CheckLoginInterceptor开始");
        HandlerMethod method = (HandlerMethod) handler;

        boolean noLoginRequired = AnnotationUtils.findAnnotation(
        		method.getMethod(), NoLogin.class) != null;
        if (noLoginRequired) {
        	  //logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
            return true;
        }

        boolean loginRequired =
            AnnotationUtils.findAnnotation(method.getBeanType(), Login.class) != null ||
                AnnotationUtils.findAnnotation(method.getMethod(), Login.class) != null;
        if (!loginRequired) {
        	 // logger.info("登录检查拦截器CheckLoginInterceptor----不需要验证");
            return true;
        }

        UserDetail<GroundUser> userDetail = (UserDetail<GroundUser>) request.getAttribute(Constants.KEY_USER_DETAIL);
        if (userDetail.getId() > 0) {
        	// logger.info("登录检查拦截器CheckLoginInterceptor----需要验证----验证通过");
        	if(userDetail.getUserDetail().getIsOriginalpassword() == ORIGINAL_PASSWORD){
        		JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
                jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_UPDATE_PWD);
                String text = objectMapper.writeValueAsString(jsonpResponse);
                HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
                return false;
        	}
            return true;
        }else{
        	  //logger.info("登录检查拦截器CheckLoginInterceptor----需要验证----验证不通过");
        }

        if (HttpUtil.isJsonRequest(request)) {
            JsonpResponse jsonpResponse = new JsonpResponse(request.getParameter("jsonp_callback"));
            jsonpResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
            String text = objectMapper.writeValueAsString(jsonpResponse);
            HttpUtil.sendResponse(response, MimeTypes.APPLICATION_JSON, "UTF-8", text);
        } else {
            UriBuilder builder = new UriBuilder(Constants.SERVER_URL_HTTPS + "/login.do");
            if (StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
                builder.add("target_url", HttpUtil.getFullRequestUrl(request));
            } else {
                String loginRedirectUrl = request.getParameter("login_redirect_url");
                if (StringUtils.isNotBlank(loginRedirectUrl)) {
                    loginRedirectUrl = securityService.getSafeRedirectUrl(loginRedirectUrl);
                    if (StringUtils.startsWith(loginRedirectUrl, "/")) {
                        loginRedirectUrl = Constants.SERVER_URL + loginRedirectUrl;
                    }
                    builder.add("target_url", loginRedirectUrl);
                }
            }
            response.sendRedirect(builder.toUri());
        }

        return false;
    }
}

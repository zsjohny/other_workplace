package com.jiuyuan.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jiuyuan.util.http.HttpUtil;

public class ExceptionInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionInterceptor.class);

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {
		super.afterCompletion(request, response, handler, e);
		if (e != null) {
			logger.error("Failed to process request. uri: {}, referer: {}, ua: {}, remoteIp: {}, message: {}",
					HttpUtil.getRequestUrl(request), HttpUtil.getReferer(request), HttpUtil.getUserAgent(request),
					HttpUtil.getClientIp(request), e.getStackTrace().length > 0 ? e.getMessage() : "");
		}
	}
}

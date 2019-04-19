package com.jiuyuan.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;


public class LogUtil {

	public static String getLogPrefix(HttpServletRequest request) {
		// TODO 抽时间将这个放在拦截器中
		return WebUtil.getWebAllUrl(request)+","+UUID.randomUUID().toString()+",";
	}

	
	
	
}

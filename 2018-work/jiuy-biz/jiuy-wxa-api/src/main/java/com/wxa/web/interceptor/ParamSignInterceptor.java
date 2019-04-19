package com.wxa.web.interceptor;

import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.util.ParamSignUtils;
import com.jiuyuan.util.VersionUtil;


/**
 * 验证参数签名
 * ParamSignUtils
 * @author Administrator
 *
 */
public class ParamSignInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ParamSignInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	// 为了测试方便，先不进行延签校验
    	 //return true;
    	
    	
    	StringBuffer requestURL = request.getRequestURL();
    	String version = request.getHeader("version");
    	if(StringUtils.isEmpty(version)){
    		logger.info("参数延签时获取version为空===================请尽快处理==============");
    		logger.info("参数延签时获取version为空===================请尽快处理==============");
    		logger.info("参数延签时获取version为空===================请尽快处理==============");
    		logger.info("参数延签时获取version为空===================请尽快处理==============");
    		 logger.info("参数延签时获取version为空，requestURL:"+requestURL);
    		 logger.info("参数延签时获取version为空===================请尽快处理==============");
    		 logger.info("参数延签时获取version为空===================请尽快处理==============");
    		 logger.info("参数延签时获取version为空===================请尽快处理==============");
    		 logger.info("参数延签时获取version为空===================请尽快处理==============");
			//如果版本号为空时暂时不进行延签，为了兼容
    		 return true;
    	}
		//小于1.3.4则不进行参数延签
    	 if(VersionUtil.lt(version, "1.3.4")){
    		 logger.info("版本需要有小于1.3.4则不进行参数延签参,请尽快升级，version:"+version+",requestURL:"+requestURL);
    		 return true;
    	 }
    	 String sign = request.getHeader("sign");
    	 if(StringUtils.isEmpty(sign)){
    		 logger.info("参数延签时获取sign为空，请尽快排查问题！！！！！！！！！！！！！！！！！！！！！");
    		 return false;
    	 }

    	 //获取所有参数
    	 HashMap<String, String> signMap = new HashMap<String, String>();
    	 Enumeration enu = request.getParameterNames();
    	 while(enu.hasMoreElements()){
    		 String paraName=(String)enu.nextElement();
    		 signMap.put(paraName,request.getParameter(paraName));
    	 }
    	 String serverSign = ParamSignUtils.getSign(signMap);
    	 if(serverSign.equals(sign)){
    		 return true;
    	 }else{
    		 logger.info("参数延签不通过:serverSign"+serverSign+"sign:"+sign);
    		 return false;
    	 }
    }
    
    


}

package org.dream.utils.mvc;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyAccessException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;


/**
 *
 * 描述:统一的未处理的异常的最终处理策略
 * <bean class="org.dream.utils.http.SpringMvcExceptionHandler"/>
 * @author  boyce
 * @created 2015年5月26日 下午2:11:09
 * @since   v1.0.0
 */
public class SpringMvcExceptionHandler implements HandlerExceptionResolver {
	Logger logger=LoggerFactory.getLogger(getClass());
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		response.setStatus(200);
		response.setHeader("Content-Type", "text/json;charset=utf-8");
		if(handler instanceof HandlerMethod){
			HandlerMethod method=(HandlerMethod) handler;
		}
		logger.error("user request "+request.getRequestURI()+" error!",ex);
		Response resp=Response.error("服务器内部异常，请联系技术人员！");
		if(ex instanceof org.springframework.validation.BindException||ex instanceof PropertyAccessException){
			resp=Response.error("请求参数格式不正确，请检查是否在数字参数那里输入了字符串！");
		}
		try {
			response.getWriter().print(JSON.toJSONString(resp));
			response.flushBuffer();
		} catch (IOException e) {
		}
		return null;
	}
}

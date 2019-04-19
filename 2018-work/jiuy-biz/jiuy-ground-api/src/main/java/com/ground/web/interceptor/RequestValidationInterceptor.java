/**
 * 
 */
package com.ground.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yujj.web.helper.NeedRequestCheck;

/**
 * @author LWS
 *
 */
public class RequestValidationInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod method = (HandlerMethod) handler;
        boolean bNeedCheck = AnnotationUtils.findAnnotation(method.getMethod(), NeedRequestCheck.class) != null;
        if(bNeedCheck){
            String requesttime = request.getParameter("req");
            if (StringUtils.isBlank(requesttime)) {
                return false;
            }

            long time = Long.parseLong(requesttime);
            if (Math.abs(time - System.currentTimeMillis()) > MAX_EXPIRATION_TIME) {
                return false;
            }else{
                return true;
            }
        }
        else{
            return true;
        }
    }
    
    private final int MAX_EXPIRATION_TIME = 1 * 60 * 1000; //1minute
}

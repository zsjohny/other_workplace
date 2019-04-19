package com.jiuy.base.Interceptor;


import com.jiuy.base.model.ControllerHandler;
import com.jiuy.base.model.MyJob;
import com.jiuy.base.model.UserSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * controller uri拦截器
 * 可以记录api调用记录
 * @author Aison
 * @date   2018/4/19 10:02
 * @version V1.0
 * @Copyright: 玖远网络
 */
public class ControllerInterceptor extends HandlerInterceptorAdapter {


    private ControllerHandler interceptorCallback;
    public ControllerInterceptor(ControllerHandler interceptorCallback) {
        this.interceptorCallback = interceptorCallback;
    }

    private String OPTIONS = "OPTIONS";
    private static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("i am in the controller handler"+ httpServletRequest.getRequestURI());
        responseThreadLocal.set(httpServletResponse);
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        // 允许x-requested-with请求头
        httpServletResponse.addHeader("Access-Control-Allow-Headers","x-requested-with");
        httpServletResponse.addHeader("Access-Control-Allow-Headers","*");
        // 允许访问的有效期
        httpServletResponse.addHeader("Access-Control-Max-Age","86400");
        String method = httpServletRequest.getMethod();
        if(OPTIONS.equals(method)){
            return false;
        }
        UserSession userSession = new UserSession();
        userSession.setUserId(655L);
        userSession.setUserName("aison");
        UserSession.setUserSession(userSession);

        boolean flag = true;
        if(interceptorCallback!=null) {
            flag =  interceptorCallback.controllerHandler(httpServletRequest,httpServletResponse,o);
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        responseThreadLocal.remove();
    }
}

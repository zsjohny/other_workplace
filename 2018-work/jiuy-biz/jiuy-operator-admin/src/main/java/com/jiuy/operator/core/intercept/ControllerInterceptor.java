package com.jiuy.operator.core.intercept;


import com.jiuy.base.model.UserSession;
import com.jiuy.operator.core.shiro.ShiroKit;
import com.jiuy.operator.core.shiro.ShiroUser;
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

    String OPTIONS = "OPTIONS";
    public static ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        responseThreadLocal.set(httpServletResponse);
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        // 允许x-requested-with请求头
        httpServletResponse.addHeader("Access-Control-Allow-Headers","x-requested-with");
        // 允许访问的有效期
        httpServletResponse.addHeader("Access-Control-Max-Age","86400");
        String method = httpServletRequest.getMethod();
        if(OPTIONS.equals(method)){
            return false;
        }

        ShiroUser shiroUser = ShiroKit.getUser();
        if(shiroUser!=null) {
            UserSession userSession = new UserSession();
            userSession.setId(shiroUser.getId().longValue());
            userSession.setName(shiroUser.getName());
            userSession.setAccount(shiroUser.getAccount());
            userSession.setDeptId(shiroUser.getDeptId());
            userSession.setPhoneNumber(shiroUser.getPhoneNumber());
            userSession.setRoleList(userSession.getRoleList());
            userSession.setStatus(shiroUser.getStatus());
            UserSession.setUserSession(userSession);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        responseThreadLocal.remove();
        UserSession.remove();
    }
}

package com.jiuy.monitoring.config;


import com.google.gson.Gson;
import com.jiuy.base.enums.GlobalsEnums;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.config.WebMvcUtil;
import com.jiuy.monitoring.controller.AcceptController;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * common aop中有一个全局的拦截 开启了跨域访问和日志记录
 * 这个注册器是当前项目的注册器..可以定制一些特殊的操作
 * 注意如果有此类 则common aop中不会有效..需要在addInterceptors中注入 common aop中的controller拦截
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/19 21:32
 * @Copyright: 玖远网络
 */
@Configuration
@Log4j2
public class MonitorWebMvcConfiguration extends WebMvcConfigurationSupport {


    @Value("${filter.query.ips}")
    private String[] ips;

    private Map<String, Boolean> passIp = new HashMap<>(6);

    @Override
    protected ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
        for (String ip : ips) {
            passIp.put(ip, true);
        }
        return super.getConfigurableWebBindingInitializer();
    }

    /**
     * 注册拦截器
     *
     * @param registry 注册类
     * @author Aison
     * @date 2018/6/8 10:56
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcUtil.initInterceptor(registry, (httpServletRequest, httpServletResponse, o) -> {

            if (!isWhiteIp(httpServletRequest, o)) {
                try {
                    sendErrorAsync(httpServletResponse, new ResponseResult(GlobalsEnums.LOG_OUT));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
            return true;
        });
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcUtil.initResource(registry);
        super.addResourceHandlers(registry);
    }


    private void sendErrorAsync(HttpServletResponse response, ResponseResult rs) {
        response.setContentType("text/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        rs.setStatus(0);
        String str = new Gson().toJson(rs);
        try {
            response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是白名单ip
     *
     * @param request request
     * @param o       o;
     * @author Aison
     * @date 2018/6/27 10:26
     */
    private boolean isWhiteIp(HttpServletRequest request, Object o) {

        if (o instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) o;
            Object bean = handlerMethod.getBean();
            if (bean instanceof AcceptController) {
                String ip = request.getRemoteAddr();
                log.info("请求ip是 =====>{} ", ip);
                Boolean pass = passIp.get(ip);
//                return pass == null ? false : pass;
                return true;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

}

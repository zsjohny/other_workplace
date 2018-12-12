package com.finace.miscroservice.commons.auth;

import com.finace.miscroservice.commons.handler.XssHttpServletRequestWrapperHandler;
import com.finace.miscroservice.commons.handler.XssUrlPathHandler;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.finace.miscroservice.commons.config.FilterConfig.INTERCEPTOR_PATH;

/**
 * XSS的注入拦截
 */
//@Configuration
public class XSSInjectInterceptor {


    /*** 不进行过滤xss白名单路径 多个利用,分开**/
    private static String whitePathList = "zipkin";


    @Bean
    public FilterRegistrationBean createXssFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XSSFilter());
        filterRegistrationBean.addUrlPatterns(INTERCEPTOR_PATH);
        return filterRegistrationBean;
    }


    /**
     * Xss的过滤器
     */
    private class XSSFilter implements Filter {


        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            chain.doFilter(new XssHttpServletRequestWrapperHandler((HttpServletRequest) request), response);
        }

        @Override
        public void destroy() {

        }
    }


    /**
     * Xss bean的处理器
     *
     * @return
     */
    @Bean
    public XssMappingPostProcessor createBeanPostProcessor() {
        return new XssMappingPostProcessor();
    }


    private class XssMappingPostProcessor implements BeanPostProcessor {

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof AbstractHandlerMapping) {
                AbstractHandlerMapping ahm = (AbstractHandlerMapping) bean;
                ahm.setUrlPathHelper(new XssUrlPathHandler());
            }

            return bean;
        }
    }


    /**
     * 清除Xss的类容
     *
     * @param content 带清除的类容
     * @return
     */
    public static String xssClean(HttpServletRequest request, String content) {
        String result = "";
        if (request != null && request.getRequestURI().contains(whitePathList)) {
            result = content;
            return result;
        }

        if (content == null) {
            return result;
        }


        //防止方法中的&被替换
        result = Jsoup.clean(content, Whitelist.basic()).replaceAll("&amp;", "&");

        return result;
    }

}

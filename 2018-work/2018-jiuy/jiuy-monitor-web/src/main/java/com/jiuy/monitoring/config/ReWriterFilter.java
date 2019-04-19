package com.jiuy.monitoring.config;

import com.jiuy.model.common.DataDictionary;
import com.jiuy.service.common.ICacheService;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 11:08
 * @Copyright 玖远网络
 */
@Log4j2
public class ReWriterFilter implements Filter {


    @Resource(name = "cacheService")
    private ICacheService cacheService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("初始化拦截器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String uri = request.getRequestURI();
        uri = uri.substring(1);
        DataDictionary dataDictionary = cacheService.getByCode(uri);
        if(dataDictionary!=null) {
            String newUri = new StringBuffer(uri).append(".htm").toString();
            log.info("重定向{} 到{}",uri,newUri);
            response.sendRedirect(newUri);
        } else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {
        log.info("关闭拦截器");
    }
}

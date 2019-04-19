package com.jiuyuan.web.filter.spring;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import com.jiuyuan.util.http.HttpUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;


public class MDCFilter extends GenericFilterBean {
	private static final Log logger = LogFactory.get();

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        try {
            HttpServletRequest request = (HttpServletRequest) req;

            MDC.put("userId", "NA");
            // 'userId' is later set in MDCInterceptor

            String uri = HttpUtil.getRequestUrl(request);
            MDC.put("uri", StringUtils.defaultIfEmpty(uri, "NA"));

            String referer = HttpUtil.getReferer(request);
            MDC.put("referer", StringUtils.defaultIfEmpty(referer, "NA"));

            String ua = HttpUtil.getUserAgent(request);
            MDC.put("ua", StringUtils.defaultIfEmpty(ua, "NA"));

            String ip = HttpUtil.getClientIp(request);
            MDC.put("ip", StringUtils.defaultIfEmpty(ip, "NA"));

            chain.doFilter(request, response);
        } finally {
            MDC.remove("userId");
            MDC.remove("uri");
            MDC.remove("referer");
            MDC.remove("ua");
            MDC.remove("ip");
//            logger.info("MDCFilter.doFilter  =====>  remove userId ");
        }
    }
}

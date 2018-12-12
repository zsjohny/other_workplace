package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.IdUtil;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.JwtToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

import static com.finace.miscroservice.commons.log.Log.clearTrace;
import static com.finace.miscroservice.commons.log.Log.traceRequest;

/**
 * 自定义拦截器
 */
@Configuration
public class FilterConfig {


    private final int FIRST_ORDER = 1;
    private final int SECOND_ORDER = 2;

    public static final String INTERCEPTOR_PATH = "/*";

    /**
     * 权限拦截
     *
     * @return
     */
    @Bean
    @ConditionalOnExpression("${filter.path.enabled}")
    public FilterRegistrationBean createPathFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new PathFilter());
        filterRegistrationBean.addUrlPatterns(INTERCEPTOR_PATH);
        filterRegistrationBean.setOrder(FIRST_ORDER);
        return filterRegistrationBean;
    }

    /**
     * 权限拦截
     *
     * @return
     */
    @Bean
    @ConditionalOnExpression("${filter.auth.enabled}")
    public FilterRegistrationBean createAuthFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AuthFilter());
        filterRegistrationBean.addUrlPatterns(INTERCEPTOR_PATH);
        filterRegistrationBean.setOrder(SECOND_ORDER);
        return filterRegistrationBean;
    }

    /**
     * path拦截器
     */
    private class PathFilter implements Filter {

        private final String CONFIG_NAME = "properties/pathFilter.properties";

        private Properties properties = new Properties();

        private Log log = Log.getInstance(PathFilter.class);

        {
            try {
                properties.load(new ClassPathResource(CONFIG_NAME).getInputStream());
            } catch (Exception e) {
                log.error("加载路径拦截器配置文件出错", e);
            }
        }

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;

            String path = req.getRequestURI();
            String[] pathArr = path.split("/");

            String allowedPaths = properties.getProperty(pathArr.length == 0 ? "" : pathArr[pathArr.length - 1]);

            Boolean allowedPassFlag = Boolean.FALSE;
            String ip = Iptools.gainRealIp(req);

            //获取可用的拦截列表
            if (StringUtils.isNotEmpty(allowedPaths)) {
                String[] pathsArr = allowedPaths.split(",");


                for (String str : pathsArr) {
                    if (StringUtils.isEmpty(str)) {
                        continue;
                    }
                    //判断header中是否包含设定的值
                    if (str.contains(":")) {
                        //形式 header的key : value
                        String[] sp = str.split(":");

                        if (req.getHeader(sp[0]).equals(sp[1])) {
                            allowedPassFlag = Boolean.TRUE;
                            break;
                        }
                        //查看是否符合Ip
                    } else if (str.equals(ip)) {
                        allowedPassFlag = Boolean.TRUE;
                        break;
                    }
                }

            } else {
                allowedPassFlag = Boolean.TRUE;
            }

            if (allowedPassFlag) {
                log.info("Ip={} 访问路径={} 允许通过", ip, path);
                traceRequest(req);
                chain.doFilter(request, response);
                clearTrace();
            } else {
                log.info("Ip={} 访问路径={} 不允许通过", ip, path);
            }

        }

        @Override
        public void destroy() {

        }
    }

    /**
     * 权限拦截器
     */
    private class AuthFilter implements Filter {

        @Override
        public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {


            HttpServletRequest req = (HttpServletRequest) request;


            if (req.getHeader(JwtToken.AUTH_SUFFIX) == null) {
                //重定向
                HttpServletResponse response = (HttpServletResponse) res;
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
                response.addHeader("Access-Control-Expose-Headers", JwtToken.TOKEN);
                response.addHeader("Access-Control-Allow-Headers", JwtToken.UID + "," + JwtToken.TOKEN);
                //设置Id
                String token = req.getHeader(JwtToken.TOKEN);
                String uid = req.getHeader(JwtToken.UID);
                if (StringUtils.isNoneEmpty(token, uid)) {
                    Integer idNu = JwtToken.parseToken(token, uid);
                    if (idNu != null) {
                        IdUtil.setId(idNu);

                    }
                }
            } else {
                //非重定向

                //设置Id
                String id = req.getHeader(JwtToken.ID);
                if (StringUtils.isNotEmpty(id)) {
                    IdUtil.setId(Integer.valueOf(id));
                }

            }

            traceRequest(req);
            chain.doFilter(request, res);
            clearTrace();

        }


        @Override
        public void destroy() {

        }
    }


}

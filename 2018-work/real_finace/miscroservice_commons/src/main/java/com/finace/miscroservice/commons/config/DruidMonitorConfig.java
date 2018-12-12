package com.finace.miscroservice.commons.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.finace.miscroservice.commons.config.FilterConfig.INTERCEPTOR_PATH;

/**
 * druid的监控配置类
 */
@Configuration
@ConditionalOnExpression("${druid.monitor.enabled}")
public class DruidMonitorConfig {

    /**
     * 创建一个druid的监控参数
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean createDruidStatViewServlet(
            @Value("${druid.monitor.allowIp}") String allowIp, @Value("${druid.monitor.name}") String userName,
            @Value("${druid.monitor.pass}") String pass) {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("allow", allowIp);
        servletRegistrationBean.addInitParameter("loginUsername", userName);
        servletRegistrationBean.addInitParameter("loginPassword", pass);
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }


    /**
     * 创建druid的拦截规则
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean createWebStatViewFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns(INTERCEPTOR_PATH);
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}

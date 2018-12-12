package com.goldplusgold.td.sltp.core.config;

import com.goldplusgold.td.sltp.core.auth.AppAnonFilter;
import com.goldplusgold.td.sltp.core.auth.AppAuthcFilter;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证与授权相关的配置（shiro）
 */

@Configuration
public class ShiroConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() throws ServletException {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy("shiroFilter");
        proxy.setTargetFilterLifecycle(true);
        filterRegistration.setFilter(proxy);
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        return filterRegistration;
    }

    @Bean
    public LifecycleBeanPostProcessor createLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean createShiroFilter(@Autowired AppAnonFilter appAnonFilter,
                                                    @Autowired AppAuthcFilter appAuthcFilter) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(new DefaultWebSecurityManager());
        //bean.setLoginUrl("/td_login_page?fromFlag=0");

        Map<String, Filter> filters = new HashMap<>();
        filters.put("appAnon", appAnonFilter);
        filters.put("appAuth", appAuthcFilter);
        bean.setFilters(filters);

        Map<String, String> chains = new HashMap<>();

        //TODO: 权限在这里添加
        chains.put("/td_account_cash_app", "appAuth");
        chains.put("/login_status", "appAnon");

        bean.setFilterChainDefinitionMap(chains);

        return bean;
    }
}

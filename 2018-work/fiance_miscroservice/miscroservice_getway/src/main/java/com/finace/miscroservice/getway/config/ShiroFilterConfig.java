package com.finace.miscroservice.getway.config;

import com.finace.miscroservice.getway.filter.UserAnonymousFilter;
import com.finace.miscroservice.getway.filter.UserAuthFilter;
import com.finace.miscroservice.getway.interpect.AccessInterceptor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

import static com.finace.miscroservice.commons.utils.JwtToken.AUTH_SUFFIX;


/**
 * ShiroFilter的配置中心
 */
@Configuration
public class ShiroFilterConfig {


    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean createShiroFactory(AccessInterceptor accessInterceptor) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        factoryBean.setSecurityManager(new DefaultWebSecurityManager());
        Map<String, Filter> filters = new HashMap<>();
        filters.put("userAnonymous", new UserAnonymousFilter(accessInterceptor));
        filters.put("userAuth", new UserAuthFilter(accessInterceptor));
        factoryBean.setFilters(filters);
        Map<String, String> chains = new HashMap<>();
        /**
         *   userAnonymous--不需要权限校验的接口
         *   userAuth--需要权限校验的接口(默认结尾auth)
         * */
        chains.put("/**/" + AUTH_SUFFIX, "userAuth");
        chains.put("/**", "userAnonymous");

        factoryBean.setFilterChainDefinitionMap(chains);


        return factoryBean;
    }


}



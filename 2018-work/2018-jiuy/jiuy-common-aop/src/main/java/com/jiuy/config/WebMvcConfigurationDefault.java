package com.jiuy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 拦截器注册类
 * @author Aison
 * @date 2018/5/19 21:32
 * @version V1.0
 * @Copyright: 玖远网络
 */
@Configuration
public class WebMvcConfigurationDefault extends WebMvcConfigurationSupport {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        WebMvcUtil.initInterceptor(registry,null);
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        WebMvcUtil.initResource(registry);
        super.addResourceHandlers(registry);
    }
}

package com.jiuy.config;

import com.jiuy.base.Interceptor.ControllerInterceptor;
import com.jiuy.base.model.ControllerHandler;
import com.jiuy.base.model.MyJob;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

/**
 * 这些是默认的 如果其他系统有覆盖 WebMvcConfigurationSupport的配置的话
 * 引用这个类的两个静态方法就可以了 不需要写额外的代码 写得到处都是乱
 * @author Aison
 * @version V1.0
 * @date 2018/6/8 11:49
 * @Copyright 玖远网络
 */
public class WebMvcUtil {


    /**
     * 静态资源策略
     * @param registry 资源注册器
     * @author Aison
     * @date 2018/6/8 11:56
     */
    public static void initResource (ResourceHandlerRegistry registry) {

        /*
         * 如果我们将/xxxx/** 修改为 /** 与默认的相同时，则会覆盖系统的配置，可以多次使用 addResourceLocations 添加目录，
         * 优先级先添加的高于后添加的。
         *
         * 如果是/xxxx/** 引用静态资源 加不加/xxxx/ 均可，因为系统默认配置（/**）也会作用
         * 如果是/** 会覆盖默认配置，应用addResourceLocations添加所有会用到的静态资源地址，系统默认不会再起作用
         */
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/templates/");

    }
    /**
     * 拦截器策略
     * @param registry 拦截器注册类
     * @author Aison
     * @date 2018/6/8 11:54
     */
    public static void initInterceptor(InterceptorRegistry registry, ControllerHandler handler) {

        //这里可以添加多个拦截器
        registry.addInterceptor(new ControllerInterceptor(handler)).addPathPatterns("/**");

    }
}

package com.jiuy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * 统一时间处理
 * @author Aison
 * @version V1.0
 * @date 2018/6/11 16:50
 * @Copyright 玖远网络
 */
@Configuration
public class DateConfig {


    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    /**
     * 增加字符串转日期的功能
     */
    @PostConstruct
    public void initEditableValidation() {
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter
                .getWebBindingInitializer();
        GenericConversionService genericConversionService = (GenericConversionService) initializer
                .getConversionService();
        genericConversionService.addConverter(new StringToDateConverter());

    }
}

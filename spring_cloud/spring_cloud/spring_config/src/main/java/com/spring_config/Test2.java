package com.finace.miscroservice.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;


public class Test2 extends PropertySourcesPlaceholderConfigurer {




    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, ConfigurablePropertyResolver propertyResolver) throws BeansException {
        super.processProperties(beanFactoryToProcess, propertyResolver);


        String property = propertyResolver.getProperty("server.port");

        System.out.println(property);


//        propertyResolver.setConversionService();


    }

}

package com.e_commerce.miscroservice.authorize.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

public class configs implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
        System.out.println("-----------------------------------");
    }
}

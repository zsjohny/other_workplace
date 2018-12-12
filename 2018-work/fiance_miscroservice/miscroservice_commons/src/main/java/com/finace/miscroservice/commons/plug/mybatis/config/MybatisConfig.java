package com.finace.miscroservice.commons.plug.mybatis.config;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * mybatis的配置中心
 */
public class MybatisConfig extends MapperScannerConfigurer {

    public static ApplicationContext APPLICATION_CONTEXT;

    public static Boolean IS_CAMEL = Boolean.TRUE;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
        super.setApplicationContext(applicationContext);

    }


    public void setIsCamel(Boolean isCamel) {
        IS_CAMEL = isCamel;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.setBasePackage("com.finace.miscroservice.commons.plug.mybatis.mapper*");
        super.afterPropertiesSet();

    }
}

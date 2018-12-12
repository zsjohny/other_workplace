package com.finace.miscroservice.commons.config;


import com.finace.miscroservice.commons.auth.DynamicDataSourceInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 动态数据源的配置
 */
@Configuration
@ConditionalOnExpression("${datasource.enabled}")
public class DynamicDataSourceConfig {


    @Bean
    public DynamicDataSourceInterceptor createDynamicDataSourceInterceptor() {
        return new DynamicDataSourceInterceptor();
    }

}

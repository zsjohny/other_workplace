package com.finace.miscroservice.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * 配置中心的重试配置
 */
public class ConfigureCenterRetryConfig {


    /**
     * 初始化重试时间
     */
    private final long initialInterval = 1000L;

    /**
     * 最大尝试次数
     */
    private final int maxAttempts = 10;


    /**
     * 最大的尝试间隔
     */
    private final long maxInterval = 5000L;


    /**
     * 每次尝试的间隔
     */
    private final double multiplier = 1.4D;


    /**
     * 创建重试的bean (bean的configServerRetryInterceptor名称不能改动)
     *
     * @return
     */
    @Bean
    public RetryOperationsInterceptor configServerRetryInterceptor() {
        return RetryInterceptorBuilder
                .stateless()
                .backOffOptions(initialInterval, multiplier, maxInterval)
                .maxAttempts(maxAttempts)
                .build();

    }


}
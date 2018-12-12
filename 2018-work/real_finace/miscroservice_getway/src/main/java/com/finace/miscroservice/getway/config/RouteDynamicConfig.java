package com.finace.miscroservice.getway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.HashOperations;

@Configuration
public class RouteDynamicConfig {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ZuulProperties zuulProperties;


    @Autowired
    @Qualifier("zuulHashRedisTemplate")
    private HashOperations<String, String, String> zuulHashRedisTemplate;

    @Bean
    public RouteLocator createGetWayDynamicConfig() {

        return new ZuulDynamicConfig(serverProperties.getServletPrefix(), zuulProperties, zuulHashRedisTemplate);

    }


}

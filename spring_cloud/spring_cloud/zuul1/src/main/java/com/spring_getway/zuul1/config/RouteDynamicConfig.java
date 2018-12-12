package com.spring_getway.zuul1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteDynamicConfig {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private ZuulProperties zuulProperties;

    @Bean
    public RouteLocator createZuulDynamicConfig() {
        return new ZuulDynamicConfig(serverProperties.getServletPrefix(), zuulProperties);

    }


}

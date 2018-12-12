package com.spring_rpc.ribbon.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration

public class RibbonConfig {
    @Bean
    @LoadBalanced
    public RestTemplate createTemplate() {
        return new RestTemplate();
    }

}

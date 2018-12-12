package com.finace.miscroservice.borrow.config;


import com.finace.miscroservice.commons.handler.impl.RedisDistributeLockHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributeLockConfig {

    @Bean
    public RedisDistributeLockHandler getDistributeLock(){
        return new RedisDistributeLockHandler();
    }


}

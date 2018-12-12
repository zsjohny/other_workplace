package com.finace.miscroservice.module.activity1.config;

import com.finace.miscroservice.module.activity1.entity.Redis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RedisConfig {


    @PostConstruct
    public void init() {
        System.out.println("我初始化了111111111111");
    }

    @Bean
    public Redis newRedis() {
        return new Redis();
    }


}

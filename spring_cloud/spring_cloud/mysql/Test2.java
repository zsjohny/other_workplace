package com.finace.miscroservice.task_scheduling.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Test2 {

    @Bean
    public MybatisPage c() {
        return new MybatisPage();
    }

}

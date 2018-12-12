package com.spring_getway.zuul1.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Test implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.err.println("hello");
    }


}

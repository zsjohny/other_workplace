package com.spring_config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ConfigClient1Main {


    public static void main(String[] args) {
        SpringApplication.run(ConfigClient1Main.class, args);
    }
}

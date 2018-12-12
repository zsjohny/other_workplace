package com.spring_config_bus1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SpringBus1Main {


    public static void main(String[] args) {
        SpringApplication.run(SpringBus1Main.class, args);
    }
}

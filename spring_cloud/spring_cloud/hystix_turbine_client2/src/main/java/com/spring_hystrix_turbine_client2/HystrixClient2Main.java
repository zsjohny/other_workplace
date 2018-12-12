package com.spring_hystrix_turbine_client2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableEurekaClient
@SpringBootApplication
@EnableHystrix
public class HystrixClient2Main {


    public static void main(String[] args) {
        SpringApplication.run(HystrixClient2Main.class, args);
    }
}

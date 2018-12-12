package com.spring_hystrix_turbine_client1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@EnableEurekaClient
@EnableHystrix
@SpringBootApplication
public class HystrixClient1Main {


    public static void main(String[] args) {
        SpringApplication.run(HystrixClient1Main.class, args);
    }
}

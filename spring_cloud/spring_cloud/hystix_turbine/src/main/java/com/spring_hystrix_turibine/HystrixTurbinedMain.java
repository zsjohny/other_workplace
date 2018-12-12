package com.spring_hystrix_turibine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableTurbine
@EnableEurekaClient
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixTurbinedMain {


    public static void main(String[] args) {
        SpringApplication.run(HystrixTurbinedMain.class, args);
    }
}

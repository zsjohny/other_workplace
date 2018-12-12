package com.spring_server1.ribbon.active;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApp1Server {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp1Server.class, args);
    }
}

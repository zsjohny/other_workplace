package com.spring_server2.ribbon.active;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApp2Server {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApp2Server.class, args);
    }
}

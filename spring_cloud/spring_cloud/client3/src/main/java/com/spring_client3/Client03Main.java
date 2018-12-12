package com.spring_client3;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class Client03Main {

    public static void main(String[] args) {
        SpringApplication.run(Client03Main.class, args);
    }


}

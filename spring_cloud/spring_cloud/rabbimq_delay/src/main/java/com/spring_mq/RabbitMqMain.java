package com.spring_mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.concurrent.locks.LockSupport;

@SpringBootApplication
public class RabbitMqMain {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            System.out.println("========================");
            while (true) {
                rabbitTemplate.convertAndSend("delay_routing_key_100", "333333333333");

                LockSupport.parkNanos(50000);
            }


        }).start();


    }


    public static void main(String[] args) {
        SpringApplication.run(RabbitMqMain.class, args);

    }
}

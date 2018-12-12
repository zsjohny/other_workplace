package com.shenofusc.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProducerService {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(String msg) {
        amqpTemplate.convertAndSend(msg);
        System.out.println("Sent: " + msg);
    }

    public static void main(String[] args) {
        ApplicationContext context = new GenericXmlApplicationContext("classpath:/spring-rabbitmq.xml");
        AmqpTemplate amqpTemplate = context.getBean(AmqpTemplate.class);
        amqpTemplate.convertAndSend("Hello World");
        System.out.println("Sent: Hello World");
    }

}
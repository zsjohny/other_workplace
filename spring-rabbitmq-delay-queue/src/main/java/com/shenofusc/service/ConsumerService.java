package com.shenofusc.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConsumerService {

	@Resource
	private AmqpTemplate amqpTemplate;

	public void recive() {
		System.out.println("Received: " + amqpTemplate.receiveAndConvert());
	}

    public static void main(String[] args) {
		ApplicationContext context = new GenericXmlApplicationContext("classpath:/spring-rabbitmq.xml");
		AmqpTemplate amqpTemplate = context.getBean(AmqpTemplate.class);
		System.out.println("Received: " + amqpTemplate.receiveAndConvert());
	}

}

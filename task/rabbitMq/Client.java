package com.goldplusgold.td.sltp.core.auth;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ness on 2017/5/18.
 */
public class Client {
    public static void main(String[] args) throws IOException, TimeoutException {
        String QUEUE_NAME = "1232";
        String exchange = "123";
        String routingKey = "2456";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.65");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println(new String(body, "utf-8"));
            }
        };
        channel.queueBind(QUEUE_NAME, exchange, routingKey);
        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}

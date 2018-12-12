package com.goldplusgold.td.sltp.core.auth;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Ness on 2017/5/18.
 */
public class Server {

    public static void main(String[] args) throws IOException, TimeoutException {
        String QUEUE_NAME = "1232";
        String exchange = "123";
        String routingKey = "2456";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.1.65");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);

        channel.queueBind(QUEUE_NAME, exchange, routingKey);

        channel.basicPublish(exchange, routingKey, null, "dsssss".getBytes());
    }


}

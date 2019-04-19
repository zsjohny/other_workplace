///**
// * 
// */
//package com.yujj.test.util.rabbitmq;
//
//import java.io.IOException;
//
//import com.rabbitmq.client.AMQP;
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import com.rabbitmq.client.ConnectionFactory;
//import com.rabbitmq.client.Consumer;
//import com.rabbitmq.client.DefaultConsumer;
//import com.rabbitmq.client.Envelope;
//
///**
// * @author Never
// */
//public class ReceiveLogsTopic {
//    private static final String EXCHANGE_NAME = "topic_logs";
//
//    public static void main(String[] argv) throws Exception {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("121.43.232.149");
//        Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel();
//
//        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
//        String queueName = channel.queueDeclare().getQueue();
//
//        if (argv.length < 1) {
//            System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
//            System.exit(1);
//        }
//
//        for (String bindingKey : argv) {
//            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
//        }
//
//
//        Consumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
//                                       byte[] body) throws IOException {
//                String message = new String(body, "UTF-8");
//            }
//        };
//        channel.basicConsume(queueName, true, consumer);
//    }
//}

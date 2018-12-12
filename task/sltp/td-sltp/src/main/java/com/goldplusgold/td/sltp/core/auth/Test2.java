//package com.goldplusgold.td.sltp.core.auth;
//
//import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
//import org.springframework.amqp.support.converter.ClassMapper;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.core.annotation.Order;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created by Ness on 2017/5/18.
// */
////@Configuration
////@Order(0)
//public class Test2 {
//    private final String exchange = "hello";
//    private final String queue = "1111";
//    private final String routingKey = "22455";
//
//
//    @PostConstruct
//    public void init() {
//        new Thread(() -> {
//            while (true) {
//                System.out.println("_-------");
//                UserSltpRecord userSltpRecord = new UserSltpRecord();
//                userSltpRecord.setSlPrice(24.00);
//                userSltpRecord.setUserId("1111");
//                userSltpRecord.setContract("22222");
//                userSltpRecord.setUuid("220100202");
//                userSltpRecord.setLots(1);
//                userSltpRecord.setBearBull(1);
//                userSltpRecord.setCommissionPrice(24.00);
//                _temp.convertAndSend(routingKey, userSltpRecord);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }
//
//    @Bean(name = "_conn")
//    public CachingConnectionFactory createConnect() {
//        CachingConnectionFactory factory = new CachingConnectionFactory();
//        factory.setHost("192.168.1.65");
//        return factory;
//
//    }
//
//
//    @Autowired
//    private Binding binding;
//
//    private final Binding _test = null;
//
//
////    @Autowired
////    @Qualifier("_con")
////    CachingConnectionFactory _con;
//
//    @Autowired
//    private Queue queues;
//
//    @Bean
//    public DirectExchange createExchange() {
//        return new DirectExchange(exchange, true, false);
//    }
//
//
//    @Bean("_contQ")
//    public Queue createQueue() {
//        return new Queue(queue);
//    }
//
//    @Bean("_bind")
//    public Binding createBindIng(DirectExchange directExchange, Queue queue) {
//
//        return BindingBuilder.bind(queue).to(directExchange).with(routingKey);
//
//    }
//
////    @Bean(name = "_simple")
////    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
////        SimpleRabbitListenerContainerFactory factorys = new SimpleRabbitListenerContainerFactory();
////        factorys.setConnectionFactory(factory);
////        factorys.setMessageConverter(new Jackson2JsonMessageConverter());
////        return factorys;
////
////    }
//
//
//    @Bean
//    public MessageListenerAdapter createListener(Test3 test3) {
//        MessageListenerAdapter adapter = new MessageListenerAdapter(test3, "process");
//        adapter.setMessageConverter(new Jackson2JsonMessageConverter());
//
//        return adapter;
//    }
//
//    @Bean
//    public SimpleMessageListenerContainer createContainer(Queue queue, MessageListenerAdapter adapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setQueues(queue);
//        container.setConnectionFactory(factory);
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        //从监听端得到服务端的解码
////        converter.setClassMapper(new ClassMapper() {
////            @Override
////            public void fromClass(Class<?> clazz, MessageProperties properties) {
////
////            }
////
////            @Override
////            public Class<?> toClass(MessageProperties properties) {
////                System.out.println(properties);
////                properties.setHeader("__TypeId__", "com.goldplusgold.td.sltp.core.auth.UserSltpRecord");
////                return null;
////            }
////        });
//
//        container.setMessageConverter(converter);
//        container.setMessageListener(adapter);
//        return container;
//    }
//
//
//    @Autowired
//    @Lazy
//    @Qualifier("_conn")
//    private ConnectionFactory factory;
//
//    @Bean(name = "_temp")
//    public RabbitTemplate createTemp() {
//        RabbitTemplate template = new RabbitTemplate();
//        template.setConnectionFactory(factory);
//        template.setExchange(exchange);
//        template.setQueue(queue);
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        //从发送端告诉监听端需要解码的类
////        converter.setClassMapper(new ClassMapper() {
////            @Override
////            public void fromClass(Class<?> clazz, MessageProperties properties) {
////                properties.setHeader("__TypeId__", "com.goldplusgold.td.sltp.core.auth.UserSltpRecord");
////            }
////
////            @Override
////            public Class<?> toClass(MessageProperties properties) {
////                return null;
////            }
////        });
//        template.setMessageConverter(converter);
//        template.setRoutingKey(routingKey);
//        template.afterPropertiesSet();
//        return template;
//    }
//
//
//    @Autowired
//    @Qualifier("_temp")
//    private RabbitTemplate _temp;
//
//
//}

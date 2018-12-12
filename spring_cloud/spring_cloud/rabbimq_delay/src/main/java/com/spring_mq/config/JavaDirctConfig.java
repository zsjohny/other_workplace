package com.spring_mq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


//@Component
public class JavaDirctConfig {
    private final String LINK = "017";

    private final String DELAY_QUEUE = "delay_queue_" + LINK;
    private final String DELAY_EXCHANGE = "delay_exchange_" + LINK;
    private final String DELAY_ROUTING_KEY = "delay_routing_key_" + LINK;
    private final String DIED_TASK_QUEUE = "died_task_queue_" + LINK;
    private final String DIED_TASK_EXCHANGE = "died_task_exchange_" + LINK;
    private final String DIED_TASK_ROUTING_KEY = "died_task_routing_key_" + LINK;


    @Value("${rabbit.host}")
    private String host;
    @Value("${rabbit.name}")
    private String name;
    @Value("${rabbit.pass}")
    private String pass;

    @Bean
    public ConnectionFactory createConnectionFactory() {

        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost(host);
        factory.setUsername(name);
        factory.setPassword(pass);
        factory.setPublisherConfirms(Boolean.TRUE);
        return factory;

    }

    //##########################################################################
    //##########################################################################
    //##########################################################################

    @Bean
    public Queue createDelayQueue() {

        return QueueBuilder.durable(DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", DIED_TASK_EXCHANGE) // DLX
                .withArgument("x-dead-letter-routing-key", DIED_TASK_ROUTING_KEY) // dead letter携带的routing key
                .withArgument("x-message-ttl", 10000) //过期时间
                .build();
    }


    @Bean
    public DirectExchange createDelayExchange() {
//        return new DirectExchange(DELAY_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Binding createDelayBing() {
        return BindingBuilder.bind(createDelayQueue()).to(createDelayExchange()).with(DELAY_ROUTING_KEY);


    }

    @Bean
    public RabbitTemplate createDelayRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(createConnectionFactory());
        rabbitTemplate.setExchange(DELAY_EXCHANGE);
        rabbitTemplate.setQueue(DELAY_QUEUE);
        rabbitTemplate.setRoutingKey(DELAY_ROUTING_KEY);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.afterPropertiesSet();
        return rabbitTemplate;
    }


    //###################################################################################
    //###################################################################################
    //###################################################################################


    //###################################################################################
    //###################################################################################
    //###################################################################################

    @Bean
    public Queue createDiedTaskQueue() {
        return QueueBuilder.durable(DIED_TASK_QUEUE)
                .build();
    }


    @Bean

    public DirectExchange createDiedTaskExchange() {
//        return new DirectExchange(DIED_TASK_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
        return new DirectExchange(DIED_TASK_EXCHANGE);
    }

    @Bean
    public Binding createDiedTaskBing() {
        return BindingBuilder.bind(createDiedTaskQueue()).

                to(createDiedTaskExchange()).with(DIED_TASK_ROUTING_KEY);


    }

    @Autowired
    private MqDiedTaskListenerImpl mqDiedTaskListener;

    @Bean
    public MessageListenerAdapter createDiedTaskAdaptor() {

        MessageListenerAdapter task = new MessageListenerAdapter(mqDiedTaskListener, "task");
        return task;

    }

    @Bean
    public SimpleMessageListenerContainer createMessageListener() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(createConnectionFactory());
        container.setQueues(createDiedTaskQueue());
        container.setMessageListener(createDiedTaskAdaptor());
        container.setMessageConverter(new Jackson2JsonMessageConverter());
        return container;

    }
    //###################################################################################
    //###################################################################################
    //###################################################################################


}

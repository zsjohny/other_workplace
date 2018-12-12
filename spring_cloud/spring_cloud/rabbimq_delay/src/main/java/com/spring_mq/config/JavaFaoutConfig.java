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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;


@Component
public class JavaFaoutConfig {
    private final String LINK = "100";

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
        return factory;

    }

    //##########################################################################
    //            延迟队列 合并 死信队列
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
    public FanoutExchange createDelayExchange() {
        return new FanoutExchange(DELAY_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
    }

    @Bean
    public Binding createDelayBing() {
        return BindingBuilder.bind(createDelayQueue()).to(createDelayExchange());


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
    //           私信队列 用来接受延迟队列发送的消息
    //###################################################################################
    //###################################################################################

    @Bean
    public Queue createDiedTaskQueue() {
        return QueueBuilder.durable(DIED_TASK_QUEUE).build();
    }


    @Bean

    public FanoutExchange createDiedTaskExchange() {
        return new FanoutExchange(DIED_TASK_EXCHANGE, Boolean.TRUE, Boolean.FALSE);
    }

    @Bean
    public Binding createDiedTaskBing() {
        return BindingBuilder.bind(createDiedTaskQueue()).to(createDiedTaskExchange());


    }

    @Autowired
    private MqDiedTaskListenerImpl mqDiedTaskListener;

    @Bean
    public MessageListenerAdapter createDiedTaskAdaptor() {

        MessageListenerAdapter task = new MessageListenerAdapter(mqDiedTaskListener, "task");

        return task;

    }


    /**
     * 线程池 使用
     *
     * @return
     */
    @Bean
    public Executor createExecutor() {

        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setKeepAliveSeconds(20);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() << 1);
        taskExecutor.initialize();
        return taskExecutor;

    }

    @Bean
    public SimpleMessageListenerContainer createMessageListener() {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setTaskExecutor(createExecutor());
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

package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MQ 的配置管理中心
 */
public class MqManager {


    private Log logger = Log.getInstance(MqManager.class);

    private MqConfig mqConfig;
    /**
     * 连接工厂 仅供部分类传递使用
     */
    private Object connectionFactory;

    public void setMqConfig(MqConfig mqConfig) {
        this.mqConfig = mqConfig;
    }

    private final String MQ_CONNECTION_FACTORY_NAME = "rabbitConnectionFactory";

    /**
     * 注册mq连接工厂
     */
    public void registerConnectionFactory() {
        //检测参数
        if (checkFactoryNull() || StringUtils.isAnyEmpty(mqConfig.getHost(), mqConfig.getUserName(), mqConfig.getUserPass())) {
            logger.warn("mq所传参数为空,host={},userName={},userPass={}", mqConfig.getHost(), mqConfig.getUserName(), mqConfig.getUserPass());
            return;
        }

        //注册工厂
//        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//        beanDefinition.setBeanClass(CachingConnectionFactory.class);
//        beanDefinition.setPrimary(Boolean.TRUE);
//        beanDefinition.setAttribute("host", mqConfig.getHost());
//        beanDefinition.setAttribute("username", mqConfig.getUserName());
//        beanDefinition.setAttribute("password", mqConfig.getUserPass());
//        beanDefinition.setAttribute("publisherConfirms", Boolean.TRUE);
//
//        mqConfig.getFactory().registerBeanDefinition(MQ_CONNECTION_FACTORY_NAME, beanDefinition);

        //注册工厂
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CachingConnectionFactory.class);
        builder.addPropertyValue("host", mqConfig.getHost());
        builder.addPropertyValue("port", mqConfig.getPort());
        builder.addPropertyValue("username", mqConfig.getUserName());
        builder.addPropertyValue("password", mqConfig.getUserPass());
        builder.addPropertyValue("publisherConfirms", Boolean.TRUE);


        mqConfig.getFactory().registerBeanDefinition(MQ_CONNECTION_FACTORY_NAME, builder.getRawBeanDefinition());

    }

    /**
     * 注册mq链接的template
     */
    public void registerRabbitMqTemplate() {
        //检测参数
        if (checkFactoryNull()) {
            return;
        }

        //获取连接工厂
        getConnectionFactory();

        //注册rabbitMqTemplate
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RabbitTemplate.class);
        builder.addPropertyValue("connectionFactory", connectionFactory);
        builder.addPropertyValue("messageConverter", new Jackson2JsonMessageConverter());
        builder.setLazyInit(Boolean.FALSE);
        mqConfig.getFactory().registerBeanDefinition("rabbitMqTemplateSelf", builder.getRawBeanDefinition());


        //注册定义template
        builder = BeanDefinitionBuilder.genericBeanDefinition(MqTemplate.class);
        builder.addConstructorArgValue(mqConfig.getFactory().getBean("rabbitMqTemplateSelf"));
        builder.setLazyInit(Boolean.FALSE);
        mqConfig.getFactory().registerBeanDefinition(MqTemplate.class.getName(), builder.getRawBeanDefinition());


    }


    /**
     * 注册fanout通道并监听
     */
    public void registerFanoutListener() {
        //检测参数
        if (checkFactoryNull() || checkChannelParamsEmpty()) {
            return;
        }

        //获取连接工厂
        getConnectionFactory();

        //注册队列
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        mqConfig.getFactory().registerBeanDefinition(Queue.class.getName() + mqConfig.getQueueName(), builder.getRawBeanDefinition());

        //注册fanout交换器
        builder = BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue(Boolean.TRUE);
        builder.addConstructorArgValue(Boolean.FALSE);
        mqConfig.getFactory().registerBeanDefinition(FanoutExchange.class.getName() + mqConfig.getExchangeName(), builder.getRawBeanDefinition());

        //将队列绑定到fanout交换器 并注册
        builder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        builder.addConstructorArgValue(Binding.DestinationType.QUEUE);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue("");
        builder.addConstructorArgValue(new HashMap<String, Object>());
        mqConfig.getFactory().registerBeanDefinition(Binding.class.getName() + mqConfig.getRoutingKeyName(), builder.getRawBeanDefinition());

        //注册mq容器并初始化监听类
        builder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class);
        builder.addPropertyValue("connectionFactory", connectionFactory);
        builder.addPropertyValue("messageListener", mqConfig.getMqListenerConvert());
        builder.addPropertyValue("messageConverter", new Jackson2JsonMessageConverter());
        List<String> queueNames = new CopyOnWriteArrayList<>();
        queueNames.add(mqConfig.getQueueName());
        builder.addPropertyValue("queueNames", queueNames);
        builder.setInitMethodName("queuesChanged");
        mqConfig.getFactory().registerBeanDefinition(SimpleMessageListenerContainer.class.getName() + mqConfig.getMqListenerConvert().getClass().getName(), builder.getRawBeanDefinition());


    }

    /**
     * 注册direct通道并且监听
     */
    public void registerDirectListener() {
        //检测参数
        if (checkFactoryNull() || checkChannelParamsEmpty()) {
            return;
        }


        //获取连接工厂
        getConnectionFactory();

        //注册队列
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        mqConfig.getFactory().registerBeanDefinition(Queue.class.getName() + mqConfig.getQueueName(), builder.getRawBeanDefinition());

        //注册direct交换器
        builder = BeanDefinitionBuilder.genericBeanDefinition(DirectExchange.class);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue(Boolean.TRUE);
        builder.addConstructorArgValue(Boolean.FALSE);
        mqConfig.getFactory().registerBeanDefinition(DirectExchange.class.getName() + mqConfig.getExchangeName(), builder.getRawBeanDefinition());

        //将队列和交换器通过路由绑定并且连接和注册
        builder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        builder.addConstructorArgValue(Binding.DestinationType.QUEUE);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue(mqConfig.getRoutingKeyName());
        builder.addConstructorArgValue(Collections.<String, Object>emptyMap());
        mqConfig.getFactory().registerBeanDefinition(Binding.class.getName() + mqConfig.getRoutingKeyName(), builder.getRawBeanDefinition());

        //注册mq解析容器并且监听
        builder = BeanDefinitionBuilder.genericBeanDefinition(SimpleMessageListenerContainer.class);
        builder.addPropertyValue("connectionFactory", connectionFactory);
        builder.addPropertyValue("messageListener", mqConfig.getMqListenerConvert());
        builder.addPropertyValue("messageConverter", new Jackson2JsonMessageConverter());
        List<String> queueNames = new CopyOnWriteArrayList<>();
        queueNames.add(mqConfig.getQueueName());
        builder.addPropertyValue("queueNames", queueNames);
        builder.setInitMethodName("queuesChanged");
        mqConfig.getFactory().registerBeanDefinition(SimpleMessageListenerContainer.class.getName() + mqConfig.getMqListenerConvert().getClass().getName(), builder.getRawBeanDefinition());

    }


    /**
     * 注册fanout的ttl
     */
    public void registerFanoutTTl() {

        //检查参数
        if (checkFactoryNull() || checkChannelParamsEmpty() || checkTTlParamsEmpty()) {
            return;
        }


        //定义ttl参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", mqConfig.getTTL_EXCHANGE_NAME());// DLX
        arguments.put("x-dead-letter-routing-key", mqConfig.getTTL_ROUTING_KEY_NAME());// dead letter携带的routing key
        arguments.put("x-message-ttl", mqConfig.getTTL_DELAY_TIME());//过期时间

        //设置ttl参数
        mqConfig.setMqArguments(arguments);

        //注册队列
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        builder.addConstructorArgValue(Boolean.TRUE);
        builder.addConstructorArgValue(Boolean.FALSE);
        builder.addConstructorArgValue(Boolean.FALSE);
        builder.addConstructorArgValue(mqConfig.getMqArguments());

        mqConfig.getFactory().registerBeanDefinition(Queue.class.getName() + mqConfig.getQueueName(), builder.getRawBeanDefinition());

        //注册fanout交换器
        builder = BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue(Boolean.TRUE);
        builder.addConstructorArgValue(Boolean.FALSE);
        mqConfig.getFactory().registerBeanDefinition(FanoutExchange.class.getName() + mqConfig.getExchangeName(), builder.getRawBeanDefinition());


        //将队列绑定到fanout交换器 并注册
        builder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class);
        builder.addConstructorArgValue(mqConfig.getQueueName());
        builder.addConstructorArgValue(Binding.DestinationType.QUEUE);
        builder.addConstructorArgValue(mqConfig.getExchangeName());
        builder.addConstructorArgValue("");
        builder.addConstructorArgValue(new HashMap());

        mqConfig.getFactory().registerBeanDefinition(Binding.class.getName()
                + mqConfig.getRoutingKeyName(), builder.getRawBeanDefinition());


    }

    /**
     * 注册并获取mq的监听转换类
     *
     * @param mqListenerConvert mq监听转换类
     * @return
     */
    public Object registerAndGetMqListenerConvert(Class mqListenerConvert) {
        Object resultBean = null;

        //检查参数
        if (checkFactoryNull()) {
            return resultBean;
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(mqListenerConvert);
        String beanName = mqListenerConvert.getSimpleName() + UUIdUtil.generateName();
        mqConfig.getFactory().registerBeanDefinition(beanName, builder.getRawBeanDefinition());

        resultBean = mqConfig.getFactory().getBean(beanName);

        return resultBean;


    }


    /**
     * 检测mq的工厂是否是没有连接
     *
     * @return true为没有连接 false为连接
     */
    private boolean checkFactoryNull() {
        //检测参数
        if (mqConfig.getFactory() == null) {
            logger.warn("mq的工厂工具 没有注入");
            return true;
        }

        return false;
    }

    /**
     * 检测 mqConfig 的通道连接参数是否为空
     *
     * @return true为空 false为不空
     */
    private boolean checkChannelParamsEmpty() {

        if (StringUtils.isAnyEmpty(mqConfig.getQueueName(), mqConfig.getExchangeName(), mqConfig.getRoutingKeyName())) {
            logger.warn("mq的配置传递为空 queueName={}, exchangeName={},routingKeyName={}", mqConfig.getQueueName(), mqConfig.getExchangeName(), mqConfig.getRoutingKeyName());
            return true;
        }
        return false;

    }

    /**
     * 获取connectionFactory
     */
    private void getConnectionFactory() {
        connectionFactory = mqConfig.getFactory().getBean(MQ_CONNECTION_FACTORY_NAME);
        if (connectionFactory == null) {
            logger.warn("系统没有获取到 connectionFactory ,请先注册mq连接工厂");
            return;
        }

    }


    /**
     * 检查ttl参数是否为空 true为空 false为不空
     */
    private boolean checkTTlParamsEmpty() {
        //检查ttl特殊参数
        if (StringUtils.isAnyEmpty(mqConfig.getTTL_EXCHANGE_NAME(), mqConfig.getTTL_ROUTING_KEY_NAME()) || mqConfig.getTTL_DELAY_TIME() == null) {
            logger.warn("ttl的参数所传为空 ttl_exchange_name={} ,ttl_routing_key_name={} , ttl_delay_time={}", mqConfig.getTTL_EXCHANGE_NAME(), mqConfig.getTTL_ROUTING_KEY_NAME(), mqConfig.getTTL_DELAY_TIME());
            return true;
        }
        return false;
    }

}

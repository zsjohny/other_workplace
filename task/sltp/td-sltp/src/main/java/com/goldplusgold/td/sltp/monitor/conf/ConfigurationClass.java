package com.goldplusgold.td.sltp.monitor.conf;

import com.goldplusgold.mq.p2p.P2PChannelBus;
import com.goldplusgold.mq.p2p.P2PChannels;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import com.goldplusgold.td.sltp.monitor.listener.QuotaListener;
import com.goldplusgold.td.sltp.monitor.listener.UserOffsetListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 配置类
 * Created by Administrator on 2017/5/12.
 */
@Configuration
public class ConfigurationClass {
    @Value("${td.redis.host}")
    private String tdRedisHost;
    @Value("${td.redis.port}")
    private Integer tdRedisPort;
    @Value("${td.redis.password}")
    private String tdRedisPassword;
    @Value("${td.redis.database}")
    private Integer tdRedisDatabase;

    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${spring.redis.database}")
    private Integer redisDatabase;

    @Bean(name = "tDJedisConnectionFactory")
    @Primary
    public JedisConnectionFactory createTDJedisConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(tdRedisHost);
        connectionFactory.setPort(tdRedisPort);
        connectionFactory.setPassword(tdRedisPassword);
        connectionFactory.setDatabase(tdRedisDatabase);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "tDRedisTemplate")
    public RedisTemplate<String, String> createTDRedisTemplate(@Qualifier("tDJedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String, String> redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory createJedisConnectionFactory(){
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName(redisHost);
        connectionFactory.setPort(redisPort);
        connectionFactory.setPassword(redisPassword);
        connectionFactory.setDatabase(redisDatabase);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> createRedisTemplate(@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public QuotaListener createQuotaListener(@Qualifier("msgChannelBusRabbitMqImpl")MsgChannelBus msgChannelBus){
        QuotaListener quotaListener = new QuotaListener();
        //TODO 调试代码, 待修改
        msgChannelBus.registerSubscriber(quotaListener, PubSubChannels.CH_QUOTATION_TEST);
        return quotaListener;
    }

    @Bean
    public UserOffsetListener createUserOffsetListener(@Qualifier("p2PChannelBusRabbitImpl")P2PChannelBus p2PChannelBus){
        UserOffsetListener userOffsetListener = new UserOffsetListener();
        //TODO 调试代码, 待修改
        p2PChannelBus.registerSubscriber(userOffsetListener, P2PChannels.CH_USER_OFFSET_TEST);
        return userOffsetListener;
    }

}

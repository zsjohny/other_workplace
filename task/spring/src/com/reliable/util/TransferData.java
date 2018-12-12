package com.reliable.util;

import com.reliable.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

public class TransferData
        implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private MongoTemplate mongoTemplate;


    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        JedisPool jedisPool = new JedisPool("localhost", 10088);
        Jedis jedis = jedisPool.getResource();
        jedis.flushAll();
        List<User> list = mongoTemplate.findAll(User.class);

        for (User user : list) {
            if (user == null) {
                continue;
            }
            jedis.set("user:" + user.getPhone() + ":list", "true");
        }


        jedis.disconnect();

    }
}
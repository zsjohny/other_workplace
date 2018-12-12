package com.test;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class Main {
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @PostConstruct
    public void init() throws UnknownHostException {

        IMap<Object, Object> map = hazelcastInstance.getMap("demo.config");
        ;
        map.put("hello", "hello");


        map.put("1", "2");
        map.put("2", "3");

        new Thread(() -> {
            while (true) {
                System.out.println(map.get("1"));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {

                }
            }
        }).start();
    }

    public static void main(String[] args) throws UnknownHostException {

        SpringApplication.run(Main.class, args);
    }
}

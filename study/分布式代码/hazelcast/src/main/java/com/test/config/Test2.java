package com.test.config;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

public class Test2 {
    public static void main(String[] args) {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        IMap<Object, Object> map = hazelcastInstance.getMap("test12");

        map.put("1", "4");
        System.out.println("_____");

    }
}

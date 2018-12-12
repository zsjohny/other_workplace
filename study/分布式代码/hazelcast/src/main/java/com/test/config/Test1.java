package com.test.config;

import com.hazelcast.core.*;
import com.hazelcast.map.listener.EntryExpiredListener;

public class Test1 {

    private static class MqListener implements EntryExpiredListener {

        @Override
        public void entryExpired(EntryEvent event) {
            System.out.println(event.getKey());
            System.out.println(event.getOldValue());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();


        IMap<Object, Object> map = hazelcastInstance.getMap("demo.config");

        map.addEntryListener(new MqListener(), true);

        Object put = map.put("hello", "hello");
        System.out.println(put);

        Object hello = map.get("hello");
        System.out.println(hello);
        IMap<Object, Object> map1 = Hazelcast.newHazelcastInstance().getMap("12");

        Object hello2 = map1.get("hello" );
        System.out.println(hello2);
        map.put("1", "2");
        map.put("2", "3");
    }
}

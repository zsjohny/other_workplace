package com.test.config.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class RandomSelector {
    public static void main(String[] args) {
//        Server server1 = new Server();
//        server1.setName("server1");
//        server1.setWeight(400);
//        AtomicInteger atomicInteger = new AtomicInteger();
//        ServerMap.put(server1, atomicInteger);
//
//        atomicInteger.addAndGet(400);
//
//
//        System.out.println(ServerMap.get(server1).get());


        test();

    }

    public static void test() {
        Server server1 = new Server();
        server1.setName("server1");
        server1.setWeight(400);

        Server server2 = new Server();
        server2.setName("server2");
        server2.setWeight(200);


        Server server3 = new Server();
        server3.setName("server3");
        server3.setWeight(300);


        List<Server> list = new ArrayList<>();

        list.add(server1);
        list.add(server2);
        list.add(server3);


        for (int i = 0; i < 100; i++) {

            Server select = select1(list);
            System.out.println(select);
        }

    }


    static class Server {
        private String name;
        private Integer weight;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "Server{" +
                    "name='" + name + '\'' +
                    ", weight=" + weight +
                    '}';
        }
    }

    /**
     * 轮询
     *
     * @param servers
     * @return
     */

    public static Server select(List<Server> servers) {
        ThreadLocalRandom localRandom = ThreadLocalRandom.current();
        // 计算总比重
        int totalWeight = 0;
        for (Server server : servers) {
            totalWeight += server.getWeight();
        }
        // 按照权重选择
        int randomWeight = localRandom.nextInt(totalWeight);
        for (Server server : servers) {
            randomWeight -= server.getWeight();
            if (randomWeight < 0) {
                return server;
            }
        }
        // default
        int length = servers.size();
        return servers.get(localRandom.nextInt(length));
    }


    private static final ConcurrentMap<Server, AtomicInteger> ServerMap = new ConcurrentHashMap<>();

    /**
     * 加权的选择
     *
     * @param servers
     * @return
     */
    public static Server select1(List<Server> servers) {
        Server best = null;
        int totalWeight = 0;

        for (Server server : servers) {
            AtomicInteger weightServer = ServerMap.get(server);
            if (null == weightServer) {
                weightServer = new AtomicInteger(0);
                ServerMap.putIfAbsent(server, weightServer);
            }
            int weight = server.getWeight();
            // 加权
            weightServer.addAndGet(weight);

            totalWeight += weight;

            // 根据权选择
            if (null == best || weightServer.get() > ServerMap.get(best).get()) {
                best = server;
            }
        }

        if (null == best) {
            throw new IllegalStateException("can't select client");
        }

        // 降权
        AtomicInteger bestWeightServer = ServerMap.get(best);
        bestWeightServer.set(totalWeight - bestWeightServer.get());

        return best;
    }
}

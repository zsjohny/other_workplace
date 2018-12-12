package com.test.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DbConfig {
    @Bean
    public HazelcastInstance createHazelcast() {
        // 创建默认config对象
        Config config = new Config();
        config.getNetworkConfig();

        // 获取network元素<network></network>
        NetworkConfig netConfig = config.getNetworkConfig();

        // 设置组网起始监听端口
        netConfig.setPort(9701);

        // 获取join元素<join></join>
        JoinConfig joinConfig = netConfig.getJoin();

        // 获取multicast元素<multicast></multicast>
        MulticastConfig multicastConfig = joinConfig.getMulticastConfig();

        // 禁用multicast协议
        multicastConfig.setEnabled(Boolean.FALSE);


        //获取<tcp-ip>
        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(Boolean.TRUE);


        List<String> members = new ArrayList<>();
        members.add("192.168.89.1");
        members.add("192.168.89.134");
        tcpIpConfig.setMembers(members);

        return Hazelcast.newHazelcastInstance(config);
    }


}

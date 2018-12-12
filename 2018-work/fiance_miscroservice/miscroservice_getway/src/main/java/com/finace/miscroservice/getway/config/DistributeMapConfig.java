package com.finace.miscroservice.getway.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.map.merge.LatestUpdateMapMergePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 分布式Map集合的实现
 * <p>
 * 根据hazelcast 实现的map
 * </p>
 */
@Configuration
public class DistributeMapConfig {


    /**
     * 用于搭建的分布式服务列表
     */
    @Value("${cacheServerLists}")
    private String cacheServerLists;


    private final int START_PORT = 7001;
    private final int PORT_INCREMENT_PORT = 100;
    public static final int CACHE_LIVE_TIME =  60 * 10;

    private final String CACHE_NAME = "eTongJin";

    @Bean
    public IMap<Object, Object> createDistributeMap() {


        Config config = new Config();

        //获取网端
        NetworkConfig networkConfig = config.getNetworkConfig();

        //设置开始端口和自增的步伐
        networkConfig.setPort(START_PORT);
        networkConfig.setPortCount(PORT_INCREMENT_PORT);

        //获取join 关闭 multicast 开启tcp
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(Boolean.FALSE);

        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(Boolean.TRUE);
        tcpIpConfig.setMembers(Arrays.asList(cacheServerLists));


        //设置默认的map
        MapConfig mapConfig = config.getMapConfig(CACHE_NAME);
        mapConfig.setMergePolicy(LatestUpdateMapMergePolicy.class.getCanonicalName());
        mapConfig.setTimeToLiveSeconds(CACHE_LIVE_TIME);


        //初始化map
        return Hazelcast.newHazelcastInstance(config).getMap(CACHE_NAME);


    }


}

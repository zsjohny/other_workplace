package com.finace.miscroservice.getway.util;

import com.finace.miscroservice.commons.log.Log;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.TreeSet;

/**
 * Ip的hash算法规则
 */
public class IpHashRule extends AbstractLoadBalancerRule {
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }

    private Log log = Log.getInstance(IpHashRule.class);

    /**
     * IP的key选择
     *
     * @param loadBalancer 选择器
     * @param key          所传的选择的key
     * @return
     */
    private Server ipChoose(ILoadBalancer loadBalancer, Object key) {

        Server server = null;
        if (loadBalancer == null) {
            log.warn("路由选择为空");
            return server;

        }
        //获取可用的服务
        List<Server> aliveServers = loadBalancer.getReachableServers();

        if (!(aliveServers == null || aliveServers.isEmpty())) {
            //进行排序
            TreeSet<String> keys = new TreeSet<>();
            for (Server s : aliveServers) {
                keys.add(s.getHost());
            }

            //获取排序后第一个key
            String serverName = keys.first();

            //获取第一个server
            for (Server s : aliveServers) {
                if (serverName.equals(s.getHost())) {
                    server = s;
                    break;
                }
            }
            log.info("获取服务{}成功", server);
        }

        return server;
    }

    @Override
    public Server choose(Object key) {
        return ipChoose(getLoadBalancer(), key);
    }

}
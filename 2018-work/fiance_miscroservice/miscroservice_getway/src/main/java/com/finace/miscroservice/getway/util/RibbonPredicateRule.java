package com.finace.miscroservice.getway.util;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.JwtToken;
import com.netflix.loadbalancer.ClientConfigEnabledRoundRobinRule;
import com.netflix.loadbalancer.Server;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import static com.finace.miscroservice.commons.utils.ServerAvailableUtil.getAliveServerRandom;

/**
 * ribbon的路由规则
 */
public class RibbonPredicateRule extends ClientConfigEnabledRoundRobinRule {
    private volatile Properties properties;

    private volatile HttpServletRequest request;
    private volatile Map<String, Integer> serverListCache;


    public RibbonPredicateRule() {
    }

    private Log log = Log.getInstance(RibbonPredicateRule.class);

    public RibbonPredicateRule(Properties properties, Map<String, Integer> serverListCache, HttpServletRequest request) {
        this.properties = properties;
        this.serverListCache = serverListCache;
        this.request = request;
    }


    @Override
    public Server choose(Object key) {

        String uid = request.getHeader(JwtToken.UID);

        Object address = null;
        if (uid != null && !uid.isEmpty()) {
            address = properties.get(uid);
        }

        if (address == null) {
            String ip = Iptools.gainRealIp(request);
            log.info("用户的实际Ip={}", ip);
            address = properties.get(ip);
            if (address == null) {

                return getRandomServer();
            }

        }
        String path = request.getRequestURI();
        StringTokenizer stringTokenizer = new StringTokenizer(path, "/");

        String serverName = stringTokenizer.hasMoreTokens() ? stringTokenizer.nextToken() : "";

        //设定发布的服务地址
        Server server = new Server(address.toString(), serverListCache.get(serverName.toUpperCase()));

        log.info("请求={} 被分发到地址={}的{}服务上", request.getRequestURI(), address, serverName);


        return server;
    }


    /**
     * 随机获取服务
     *
     * @return
     */
    private Server getRandomServer() {

        Server answerServer = getAliveServerRandom(getLoadBalancer().getReachableServers());

        if (answerServer == null) {
            log.warn("获取不到可用服务");
        } else {
            log.info("获取的服务={} 协议是={}", answerServer.getMetaInfo().getAppName(), answerServer.getHostPort());
        }


        return answerServer;

    }


}


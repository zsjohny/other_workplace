package com.finace.miscroservice.commons.utils;

import com.finace.miscroservice.commons.log.Log;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.netflix.loadbalancer.Server;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 服务的可用判断util
 */
public class ServerAvailableUtil {
    private static Log log = Log.getInstance(ServerAvailableUtil.class);

    private static final LoadingCache<String, Boolean> serverUnAvailableCache = CacheBuilder.newBuilder().expireAfterAccess(1
            , TimeUnit.MINUTES).build(new CacheLoader<String, Boolean>() {
        @Override
        public Boolean load(String key) throws Exception {
            return Boolean.FALSE;
        }
    });

    /**
     * 获取随机的可用服务
     *
     * @param serverList 列表
     * @return
     */
    public static Server getAliveServerRandom(List<Server> serverList) {
        Server server = null;
        if (serverList == null || serverList.isEmpty()) {
            return server;
        }

        List<Server> variableServers = new ArrayList<>(serverList);

        Collections.shuffle(variableServers);

        List<String> hostPortList = new ArrayList<>(variableServers.size());


        for (Server tmp : variableServers) {

            if (serverUnAvailableCache.getUnchecked(tmp.getHostPort())) {
                log.warn("address cache={}  unavailable", tmp.getHostPort());
                hostPortList.add(tmp.getHostPort());
                continue;
            }

            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(tmp.getHost(), tmp.getPort()), 1000 * 3);
                server = tmp;
                break;
            } catch (Exception e) {
                log.warn("server={} address={} unavailable", tmp.getMetaInfo().getAppName(), tmp.getHostPort());
                serverUnAvailableCache.put(tmp.getHostPort(), Boolean.TRUE);
                hostPortList.add(tmp.getHostPort());
            }
        }

        //这时候获取所有的参数为空 重新置为空
        if (server == null) {
            serverUnAvailableCache.invalidateAll(hostPortList);
        }

        return server;

    }

}

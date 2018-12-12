package com.finace.miscroservice.getway.config;

import com.finace.miscroservice.commons.log.Log;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.data.redis.core.HashOperations;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.finace.miscroservice.commons.enums.RedisKeyEnum.GETWAY_DYNAMIC_CONFIG_KEY;

public class ZuulDynamicConfig extends SimpleRouteLocator implements RefreshableRouteLocator {


    private Log logger = Log.getInstance(ZuulDynamicConfig.class);


    private HashOperations<String, String, String> redisTemplate;
    private final String SERVICE_PREFIX = "SERVICE_";

    public ZuulDynamicConfig(String servletPath, ZuulProperties properties, HashOperations<String, String, String> redisTemplate) {
        super(servletPath, properties);
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {

        Map<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //从application.yml中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //加载其他地方路由信息
        routesMap.putAll(routeFormDb());


        return routesMap;
    }

    private Map<String, ZuulProperties.ZuulRoute> routeFormDb() {
        Map<String, ZuulProperties.ZuulRoute> routesDbMap = new LinkedHashMap<>();

        Map<String, String> entries = redisTemplate.entries(GETWAY_DYNAMIC_CONFIG_KEY.toKey());

        if (entries == null || entries.isEmpty()) {
            logger.info("db cache have empty routes");
            return routesDbMap;
        }
        ZuulProperties.ZuulRoute zuulRoute;

        for (Map.Entry<String, String> entry : entries.entrySet()) {
            zuulRoute = new ZuulProperties.ZuulRoute();
            logger.info("start parse route {} add service {} ", entry.getKey(), entry.getValue());
            zuulRoute.setPath(entry.getKey());
            zuulRoute.setServiceId(entry.getValue());
            zuulRoute.setId(SERVICE_PREFIX + entry.getKey());
            routesDbMap.put(zuulRoute.getPath(), zuulRoute);


        }


        return routesDbMap;
    }

    private final String AUTO_REFRESH_PREFIX = "DiscoveryClient-CacheRefreshExecutor";

    @Override
    public void refresh() {
        //禁止本地自动刷新路由 等待MQ消息处理
        if (!Thread.currentThread().getName().startsWith(AUTO_REFRESH_PREFIX)) {
            super.doRefresh();
        }
    }
}

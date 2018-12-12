package com.spring_getway.zuul.config;

import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.LinkedHashMap;
import java.util.Map;

public class ZuulDynamicConfig extends SimpleRouteLocator implements RefreshableRouteLocator {

    private ZuulProperties zuulProperties;


    public ZuulDynamicConfig(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        zuulProperties = properties;
    }

    /**
     * zuul默认实现动态刷新路由
     */
    @Override
    public void refresh() {
        doRefresh();
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
        ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
        zuulRoute.setPath("/get");
        zuulRoute.setServiceId("client");
        zuulRoute.setId("service_a");
        routesDbMap.put(zuulRoute.getPath(), zuulRoute);


        return routesDbMap;
    }

}

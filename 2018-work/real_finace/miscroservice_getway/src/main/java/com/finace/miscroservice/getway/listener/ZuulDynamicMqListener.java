package com.finace.miscroservice.getway.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.entity.Routes;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.getway.interpect.PortionPublishInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import static com.finace.miscroservice.commons.enums.RedisKeyEnum.GETWAY_DYNAMIC_CONFIG_KEY;

@Component
public class ZuulDynamicMqListener extends MqListenerConvert {


    private Log logger = Log.getInstance(ZuulDynamicMqListener.class);


    private final String PATH_PREFIX = "/";
    @Autowired
    @Qualifier("zuulHashRedisTemplate")
    private HashOperations<String, String, String> zuulHashRedisTemplate;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private PortionPublishInterceptor portionPublishInterceptor;

    @Autowired
    private RouteLocator routeLocator;


    @Override
    public void transferTo(String transferData) {
        try {
            //检测
            if (StringUtils.isEmpty(transferData)) {
                logger.info("zuul接受注册服务器所传消息为空 过滤...");
                return;
            }
            logger.info("zuul开始处理 注册服务器的路由{}", transferData);
            //转换

            Routes routes = JSONObject.parseObject(transferData, Routes.class);

            switch (routes.getOpenEnum()) {
                case ADD:
                    //存储redis
                    zuulHashRedisTemplate.put(GETWAY_DYNAMIC_CONFIG_KEY.toKey(), PATH_PREFIX + routes.getService(), routes.getService());
                    break;
                case DELETE:
                    //删除redis
                    zuulHashRedisTemplate.delete(GETWAY_DYNAMIC_CONFIG_KEY.toKey(), PATH_PREFIX + routes.getService());
                    break;
                default:
                    logger.warn("zuul接受注册服务器所传消息处理  暂时不支持其他操作");
                    return;

            }


            //刷新本地路由
            RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
            publisher.publishEvent(routesRefreshedEvent);
            //刷新监听服务的文件列表
            portionPublishInterceptor.initData();

            logger.info("zuul结束处理 注册服务器的路由{}", transferData);
        } catch (Exception e) {
            logger.error("zuul接受注册服务器 路由{} 任务出错", transferData, e);
        }

    }

}

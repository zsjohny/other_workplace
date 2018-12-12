package com.finace.miscroservice.register_discovery_v2.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.Routes;
import com.finace.miscroservice.commons.enums.MqChannelEnum;
import com.finace.miscroservice.commons.log.Log;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.netflix.appinfo.InstanceInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

@Component
@EnableAsync
public class MicroServiceChangedListener {

    private Log logger = Log.getInstance(MicroServiceChangedListener.class);

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;


    private Multimap<String, String> saveRoutes;

    {
        HashMultimap<String, String> multimap = HashMultimap.create();
        saveRoutes = Multimaps.synchronizedMultimap(multimap);

    }

    private final LoadingCache<String, Long> clientRegisterTimeCache = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build(new CacheLoader<String, Long>() {
        @Override
        public Long load(String key) throws Exception {
            return -1L;
        }

    });


    @EventListener
    @Async
    public void listenerRegister(EurekaInstanceRegisteredEvent registeredEvent) {

        InstanceInfo instanceInfo = registeredEvent.getInstanceInfo();
        if (instanceInfo == null || StringUtils.isEmpty(instanceInfo.getAppName())) {
            logger.warn("服务名称为空 无须发送注册信息");
            return;
        }
        Boolean isSend = Boolean.TRUE;

        Collection<String> routes = saveRoutes.get(instanceInfo.getAppName());
        if (!routes.isEmpty()) {
            for (String str : routes) {
                //先判断是否注册过
                if (str.equals(instanceInfo.getId())) {

                    //在判断是否过期了
                    Long saveTime = clientRegisterTimeCache.getUnchecked(instanceInfo.getId());

                    if (saveTime != -1) {
                        isSend = Boolean.FALSE;
                    }

                }

            }
        }

        if (isSend) {
            //注册
            send(Routes.OperateEnum.ADD, instanceInfo.getAppName());
            saveRoutes.put(instanceInfo.getAppName(), instanceInfo.getId());
            clientRegisterTimeCache.put(instanceInfo.getId(), System.currentTimeMillis());

        }

        logger.info("添加服务{}到注册中心", instanceInfo.getAppName());
    }

    @EventListener
    @Async
    public void listenerCancel(EurekaInstanceCanceledEvent canceledEvent) {


        if (canceledEvent == null || StringUtils.isEmpty(canceledEvent.getAppName())) {
            logger.warn("服务名称为空 无须发送销毁信息");
            return;
        }

        Collection<String> routes = saveRoutes.get(canceledEvent.getAppName());
        if (!routes.isEmpty()) {
            for (String str : routes) {
                //判断是否注册过
                if (str.equals(canceledEvent.getServerId())) {
                    send(Routes.OperateEnum.DELETE, canceledEvent.getAppName());
                    saveRoutes.remove(canceledEvent.getAppName(), canceledEvent.getServerId());
                }

            }
        }


        logger.info("移除服务{}到注册中心", canceledEvent.getAppName());

    }

    /**
     * 发送消息
     *
     * @param operateEnum 操作类型
     * @param serviceName 服务名称
     */
    private void send(Routes.OperateEnum operateEnum, String serviceName) {
        Routes routes = new Routes();
        routes.setOpenEnum(operateEnum);
        routes.setService(serviceName);
        mqTemplate.sendMsg(MqChannelEnum.SERVICE_EXCHANGE.toName(), JSONObject.toJSONString(routes));
    }


}

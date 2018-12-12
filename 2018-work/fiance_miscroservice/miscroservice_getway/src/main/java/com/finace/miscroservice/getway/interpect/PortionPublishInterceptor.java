package com.finace.miscroservice.getway.interpect;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.getway.util.RibbonPredicateRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.*;


/**
 * 灰度发布的拦截器
 */
@Configuration
public class PortionPublishInterceptor {

    /**
     * 创建路由对象
     *
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RibbonPredicateRule createPredicate() {
        return new RibbonPredicateRule(publishServerList, serverListCache, request);
    }

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private HttpServletRequest request;

    private static volatile Properties publishServerList = new Properties();
    private Map<String, Integer> serverListCache;
    /**
     * 关于配置文件的说明
     * key:value
     * key 是允许通过的uid 或者ip
     * value是需要转发的ip(没有port默认跟线上一致)
     */
    private final String propertiesName = "publish.properties";
    private Log log = Log.getInstance(PortionPublishInterceptor.class);
    File paths;

    {
        paths = new File(System.getProperty("java.io.tmpdir") + "/publish");
        if (!paths.exists()) {
            try {
                paths.mkdir();
                new File(paths.getAbsolutePath(), propertiesName).createNewFile();
            } catch (IOException e) {
                log.error("创建配置刷新文件={}文件出错", propertiesName, e);
            }
        } else {
            try {
                publishServerList.load(new InputStreamReader(new FileInputStream(new File(paths.getAbsolutePath(), propertiesName)), "utf-8"));
                log.info("文件={} 配置已经载入成功", propertiesName);
            } catch (IOException e) {
                log.error("文件={} 配置装载错误", propertiesName, e);
            }


        }

    }


    /**
     * 初始化缓存集合
     */
    @PostConstruct
    public void initData() {

        List<String> services = discoveryClient.getServices();

        if (services == null || services.isEmpty()) {
            log.warn("查询不到可用的服务信息,请等待其他服务启动结束后在启动本应用!");
            return;
        }
        serverListCache = new HashMap<>();

        for (String instanceId : services) {
            List<ServiceInstance> instances = discoveryClient.getInstances(instanceId);
            if (instanceId == null || instanceId.isEmpty()) {
                continue;
            }
            ServiceInstance serviceInstance = instances.get(0);

            serverListCache.put(serviceInstance.getServiceId().toUpperCase(), serviceInstance.getPort());

        }

        serverListCache = Collections.unmodifiableMap(serverListCache);

        //注册监听文件
        monitorPropertiesChange();

        log.info("其他服务的信息已经初始化完毕!!");
    }

    private WatchService watchService;

    /**
     * 监听文件变化
     */
    private void monitorPropertiesChange() {

        if (watchService != null) {
            return;
        }

        try {
            watchService = FileSystems.getDefault().newWatchService();


            Paths.get(paths.toURI()).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            new Thread(() -> {
                while (true) {

                    try {
                        WatchKey watchKey = watchService.take();
                        for (WatchEvent watchEvent : watchKey.pollEvents()) {
                            if (propertiesName.equals(watchEvent.context().toString())) {
                                publishServerList.clear();
                                publishServerList.load(new InputStreamReader(new FileInputStream(new File(paths.getAbsolutePath(), propertiesName)), "utf-8"));
                                log.info("文件={} 配置发生改变已经重新载入", propertiesName);
                            }
                        }
                        watchKey.reset();
                    } catch (Exception e) {

                        log.error("监听文件={}出错 ", propertiesName, e);
                    }


                }
            }).start();


        } catch (Exception e) {
            log.error("注册监听文件={}出错", propertiesName, e);
        }


    }


}



package com.finace.miscroservice.controller;

import com.finace.miscroservice.commons.annotation.InnerRestController;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.config.ConfigPathConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.finace.miscroservice.commons.enums.RedisKeyEnum.GETWAY_DYNAMIC_CONFIG_KEY;

/**
 * 刷新配置接受类
 */
@InnerRestController
@RefreshScope
public class RefreshConfigController {

    private Log log = Log.getInstance(RefreshConfigController.class);


    private final String GET_REQUEST = "GET";
    private final String POST_REQUEST = "POST";

    @Autowired
    @Qualifier("zuulHashRedisTemplate")
    private HashOperations<String, String, String> redisTemplate;


    /**
     * 配置缓存类
     */
    public static Map<String, Properties> propCache = new ConcurrentHashMap<>();

    @Autowired
    private ConfigPathConfig configPathConfig;


    @Value("${spring.application.name}")
    private String applicationName;


    @Autowired
    private EurekaClient eurekaClient;

    /**
     * 检测并且获取路由
     *
     * @return
     */
    private Set<String> checkAndGet() {

        Set<String> reloadRoutes = new HashSet<>();
        FileInputStream fis = null;
        try {
            File[] load = configPathConfig.load();

            if (load == null) {
                log.info("config没有获取到本地缓存的文件");
                return reloadRoutes;
            }
            //判断第一次是否能获取本地 如果不能重新赋值
            if (propCache.isEmpty()) {
                configPathConfig.init();
            }

            Properties properties;
            Set<Map.Entry<Object, Object>> _cacheVal;

            for (File file : load) {
                if (!file.getName().endsWith(".properties")) {
                    continue;
                }
                properties = new Properties();
                if (fis != null) {
                    //关闭文件 退出文件占用
                    fis.close();
                }
                fis = new FileInputStream(file);
                properties.load(fis);
                //检查是否为空
                if (properties.isEmpty()) {
                    log.info("配置={}为空不予处理", file.getName());
                    continue;
                }

                Properties _cache = propCache.get(file.getName());

                _cacheVal = _cache.entrySet();

                for (Map.Entry<Object, Object> entries : _cacheVal) {

                    if (!entries.getValue().toString().equals(properties.getProperty(entries.getKey().toString()))) {
                        log.info("配置文件={} 值={} 发生了改变 需要进行refresh", entries.getKey(), entries.getValue());
                        //获取模块名称
                        reloadRoutes.add(file.getName().split("-")[0]);
                        //重新存储本地缓存
                        propCache.put(file.getName(), properties);
                        break;
                    }
                }


            }


        } catch (IOException e) {
            log.error("操作检查配置文件失败", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("操作文件更新,文件打开流关闭出错", e);
                }
            }
        }

        return reloadRoutes;
    }


    @PostMapping("allRefresh")
    public void refresh() {
        log.info("开始刷新公共配置类");
        Map<String, String> routes = redisTemplate.entries(GETWAY_DYNAMIC_CONFIG_KEY.toKey());

        if (routes == null || routes.isEmpty()) {
            log.info("在线模块为空,不可刷新配置");
            return;
        }
        //初始化执行刷新配置的请求防止 其余服务都存活
        access(applicationName, GET_REQUEST);

        Set<String> reloadRoutes = checkAndGet();
        if (reloadRoutes.isEmpty()) {
            log.info("配置没有改变无需进行refresh");
        } else {

            for (String route : reloadRoutes) {
                access(route, POST_REQUEST);
                log.info("模块={} 刷新完成", route);
            }

        }


        log.info("结束刷新公共配置类");
    }

    /**
     * 访问刷新
     *
     * @param path 访问模块
     */
    public void access(String path, String method) {
        HttpURLConnection conn = null;
        try {
            InstanceInfo config = eurekaClient.getNextServerFromEureka(path.toUpperCase(), Boolean.FALSE);
            String url = String.format(config.getHomePageUrl() + "bus/refresh?destination=%s:**", path);
            log.info("开始发url={} 进行配置刷新", url);
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("user-agent", "etongjin");
            conn.setConnectTimeout(3000);
            conn.connect();
            conn.getInputStream();

        } catch (IOException e) {
            log.error("刷新模块={} 配置出错", path, e);
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }


    }


}

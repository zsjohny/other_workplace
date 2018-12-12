package com.finace.miscroservice.config;

import com.finace.miscroservice.commons.log.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.finace.miscroservice.controller.RefreshConfigController.propCache;

/**
 * Path的管理配置
 */
@Configuration
public class ConfigPathConfig implements CommandLineRunner {
    private Log log = Log.getInstance(ConfigPathConfig.class);
    private final String CONFIG_PATH_PREFIX = "config-repo-";

    @Override
    public void run(String... args) throws Exception {
        cleanAndInit();
    }

    /**
     * 清理目录并初始化
     */
    private void cleanAndInit() {
        log.info("开始清除多余的config");
        File[] files = new File(System.getProperty("java.io.tmpdir")).listFiles();
        if (files.length == 0) {
            log.warn("没有多余的config需要清除");
            return;
        }

        for (File file : files) {
            if (file.getName().startsWith(CONFIG_PATH_PREFIX)) {
                if (file.isDirectory()) {
                    deleteDirect(file);
                } else {
                    file.delete();
                }
                log.info("config配置文件={} 清理成功", file.getName());
            }
        }


        log.info("结束清除多余的config");


        //初次加载参数
        Thread thread = new Thread("config-load") {
            @Override
            public void run() {

                try {
                    TimeUnit.SECONDS.sleep(5);
                    init();
                } catch (Exception e) {
                    log.error("初次加载参数出错", e);
                }
            }
        };
        thread.setDaemon(Boolean.TRUE);
        thread.start();


    }


    /**
     * 加载首次本地的缓存配置
     */
    public void init() {

        FileInputStream fis = null;
        try {
            File[] load = load();

            if (load == null) {
                log.info("当前config没有加载到本地缓存配置类");
                return;
            }

            Properties properties;
            for (File file : load) {
                if (!file.getName().endsWith(".properties")) {
                    continue;
                }
                properties = new Properties();

                if (fis != null) {
                    //及时退出文件占用
                    fis.close();
                }
                fis = new FileInputStream(file);

                properties.load(fis);

                //检查是否为空
                if (properties.isEmpty()) {
                    log.info("配置={}为空不予处理", file.getName());
                    continue;
                }
                propCache.put(file.getName(), properties);

                log.info("config成功加载了={}的缓存配置", file.getName());


            }
        } catch (IOException e) {
            log.error("config配置加载出错", e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.error("config文件打开流关闭出错", e);
                }
            }
        }

    }

    /**
     * 递归删除空目录
     *
     * @param file 文件夹
     */
    private void deleteDirect(File file) {
        File[] childFile = file.listFiles();

        //分级删除
        for (File tmpFile : childFile) {

            if (tmpFile.isDirectory()) {
                deleteDirect(tmpFile);
            } else {
                tmpFile.delete();
            }
        }

        //删除此目录
        if (file.isDirectory()) {
            file.delete();
        }
    }

    /**
     * 加载配置文件
     *
     * @return
     */
    public File[] load() {
        File[] resultFile = null;
        File[] files = new File(System.getProperty("java.io.tmpdir")).listFiles();
        if (files.length == 0) {
            log.warn("tmp 目录没有任何文件");
            return resultFile;

        }

        for (File file : files) {
            if (file.getName().startsWith(CONFIG_PATH_PREFIX)) {
                resultFile = file.listFiles();
                break;

            }
        }

        log.info("加载config文件成功");

        return resultFile;
    }

}

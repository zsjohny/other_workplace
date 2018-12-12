package com.finace.miscroservice.activity.config;

import com.alipay.jarslink.api.impl.ModuleLoaderImpl;
import com.alipay.jarslink.api.impl.ModuleManagerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 热部署相关的配置
 */
@Configuration
public class HotDeployConfig {
    @Bean
    public ModuleLoaderImpl createModuleLoader() {
        return new ModuleLoaderImpl();
    }

    @Bean
    public ModuleManagerImpl createModuleManager() {
        return new ModuleManagerImpl();
    }

}

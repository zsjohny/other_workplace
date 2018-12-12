package com.finace.miscroservice.getway.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

import static com.finace.miscroservice.getway.config.ConfigRibbonConfig.CONFIG_SERVICE_NAME;

/**
 * 配置服务的ribbon客户端配置
 */
@Configuration
@RibbonClient(name = CONFIG_SERVICE_NAME, configuration = IpHashRuleConfig.class)
public class ConfigRibbonConfig {

    /**
     * 配置中心服务的名称
     */
    public static final String CONFIG_SERVICE_NAME = "config";


}

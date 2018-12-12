package com.finace.miscroservice.getway.config;

import com.finace.miscroservice.commons.annotation.Exclude;
import com.finace.miscroservice.getway.util.IpHashRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * IP的hash自定义规则配置
 */
@Configuration
@Exclude
public class IpHashRuleConfig {
    /**
     * 创建自定义扩展的负载均衡规则
     *
     * @return
     */
    @Bean
    @Primary
    public IRule createExtendRibbonRule() {
        IpHashRule ipHashRule = new IpHashRule();
        return ipHashRule;
    }
}

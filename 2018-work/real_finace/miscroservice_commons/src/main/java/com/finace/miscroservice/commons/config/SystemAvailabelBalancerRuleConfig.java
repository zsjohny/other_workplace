package com.finace.miscroservice.commons.config;

import com.finace.miscroservice.commons.log.Log;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import static com.finace.miscroservice.commons.utils.ServerAvailableUtil.getAliveServerRandom;

/**
 * 客户端调用系统服务的可用性的配置
 */
@Configuration
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Primary
@ConditionalOnExpression("${datasource.enabled}")
public class SystemAvailabelBalancerRuleConfig extends AbstractLoadBalancerRule {
    private Log log = Log.getInstance(SystemAvailabelBalancerRuleConfig.class);

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }


    @Override
    public Server choose(Object key) {

        Server answerServer = getAliveServerRandom(getLoadBalancer().getReachableServers());

        if (answerServer != null) {

            log.info("choose server={}  random address={}",
                    answerServer.getMetaInfo().getAppName(), answerServer.getHostPort());
        } else {
            log.info("no server choose");

        }
        return answerServer;
    }

}

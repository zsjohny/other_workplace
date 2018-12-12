package com.finace.miscroservice.getway.config;

import com.finace.miscroservice.commons.config.LimitedFlowConfig;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Ip拦截
 */
@Configuration
@RefreshScope
public class AccessInterceptorConfig {

    private final String IP_INTERCEPTOR_REDIS_KEY = "ipInterceptor";

    @Value("${ip.second.interceptor.count}")
    private int interceptorCount;


    /**
     * 限流配置类
     */
    @Autowired
    private LimitedFlowConfig limitedFlowConfig;

    /**
     * IP限流大小的缓存
     */
    private int ipInterceptorCountCache;

    /**
     * 初始化一些必备参数
     */
    @PostConstruct
    public void init() {
        //初始化限流的参数
        limitedFlowConfig.init(IP_INTERCEPTOR_REDIS_KEY, interceptorCount);

        //设定缓存大小
        ipInterceptorCountCache = interceptorCount;

    }


    /**
     * 检测是否通过
     * @return  true能通过 false不能通过
     */
    public boolean checkPass() {

        boolean passFlag = Boolean.FALSE;


        //检测限流值是否更改过
        if (ipInterceptorCountCache != interceptorCount) {
            //重新设置限流大小
            limitedFlowConfig.resetLimiterCount(IP_INTERCEPTOR_REDIS_KEY, interceptorCount);
            //重新赋值
            ipInterceptorCountCache = interceptorCount;
        }


        //判断当前是否能获取令牌
        if (limitedFlowConfig.acquire(IP_INTERCEPTOR_REDIS_KEY)) {
            passFlag = Boolean.TRUE;
        }
        return passFlag;


    }


}

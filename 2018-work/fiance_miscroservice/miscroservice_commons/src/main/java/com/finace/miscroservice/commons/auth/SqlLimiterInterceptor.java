package com.finace.miscroservice.commons.auth;

import com.finace.miscroservice.commons.config.DbConfig;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.concurrent.Semaphore;

/**
 * sqL的频率拦截器
 */
@Component
@ConditionalOnBean(DbConfig.class)
@Intercepts(@Signature(type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}))
public class SqlLimiterInterceptor implements Interceptor {

    private static final Semaphore LIMITER = new Semaphore(1);


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        Object proceed;
        try {
            LIMITER.acquire();
            proceed = invocation.proceed();


        } finally {
            LIMITER.release();
        }
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        //ignore
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        //ignore
    }
}

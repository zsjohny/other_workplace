package com.finace.miscroservice.commons.auth;

import com.finace.miscroservice.commons.config.DbConfig;
import com.finace.miscroservice.commons.log.Log;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.finace.miscroservice.commons.config.DbConfig.READ_DATA_SOURCE;

/**
 * 动态数据源切换的拦截
 */
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class DynamicDataSourceInterceptor implements Interceptor {

    private Log log = Log.getInstance(DynamicDataSourceInterceptor.class);

    public static final LoadingCache<String, Boolean> DYNAMIC_DATASOURCE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build(new CacheLoader<String, Boolean>() {
        @Override
        public Boolean load(String key) throws Exception {
            return Boolean.TRUE;
        }

    });


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.info("sql={}开始查询", ((MappedStatement) invocation.getArgs()[0]).getBoundSql(invocation.getArgs()[1]).getSql());
        if (DYNAMIC_DATASOURCE_CACHE.getUnchecked(READ_DATA_SOURCE)) {

//            DbConfig.DataSourceHolder.setHolder(READ_DATA_SOURCE);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {

    }
}

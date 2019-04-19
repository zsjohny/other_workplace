package com.store.db;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Properties;

@Intercepts(value = {@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class})})
@Slf4j
public class DynamicDataSourceInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
        log.info("sql={}开始执行", ms.getBoundSql(invocation.getArgs()[1]).getSql().replaceAll("\\s{2,}|\t|\r|\n", ""));


        SqlCommandType commandType = ms.getSqlCommandType();


        if (commandType.equals(SqlCommandType.SELECT)&& !TransactionSynchronizationManager.isActualTransactionActive()) {
            HandleDataSource.putDataSource("read");

            log.info("切换到读服务器");
            return invocation.proceed();

        } else if (commandType.equals(SqlCommandType.INSERT)) {


        } else if (commandType.equals(SqlCommandType.UPDATE)) {


        } else if (commandType.equals(SqlCommandType.DELETE) ) {

        }

        HandleDataSource.putDataSource("write");

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

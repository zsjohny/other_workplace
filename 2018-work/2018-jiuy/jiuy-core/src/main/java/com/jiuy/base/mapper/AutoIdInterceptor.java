package com.jiuy.base.mapper;


import java.util.Properties;

import com.github.pagehelper.PageHelper;
import com.jiuy.base.model.Query;
import com.jiuy.base.util.Biz;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class AutoIdInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation paramInvocation) throws Throwable {
        Object[] args = paramInvocation.getArgs();
        MappedStatement statement = (MappedStatement) args[0];

        String str = statement.getSqlCommandType().toString();
        if (str.equals("INSERT")) {

        } else {
            //这里是自动分页
            Object paramArgs = args[1];
            if (Query.class.isAssignableFrom(paramArgs.getClass())) {
                Query query = (Query) paramArgs;
                if (Biz.isNotEmpty(query.getOffset(), query.getLimit())) {
                    Integer offset = query.getOffset();
                    offset = offset< 1 ? 1 :offset;
                    PageHelper.startPage(offset, query.getLimit());
                }
                if (Biz.isNotEmpty(query.getOrderBy())) {
                    PageHelper.orderBy(Biz.filterSqlString(query.getOrderBy()));
                }
            }
        }
        return paramInvocation.proceed();
    }

    @Override
    public Object plugin(Object paramObject) {
        if (paramObject instanceof Executor) {
            return Plugin.wrap(paramObject, this);
        } else {
            return paramObject;
        }
    }

    @Override
    public void setProperties(Properties paramProperties) {

    }

}
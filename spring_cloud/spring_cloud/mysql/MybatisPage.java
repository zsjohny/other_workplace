package com.finace.miscroservice.task_scheduling.test;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

//Signature定义被拦截的接口方法，可以有一个或多个。
//Executor中query方法的参数类型
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class}))
public class MybatisPage implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        System.out.println(invocation);
        invocation.getArgs();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        //判断是否是本拦截器需要拦截的接口类型，如果是增加代理
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        }
        //如果不是返回源对象。
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        //可以设置拦截器的属性
    }
}

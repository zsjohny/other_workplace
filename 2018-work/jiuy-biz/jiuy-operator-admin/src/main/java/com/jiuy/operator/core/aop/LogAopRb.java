package com.jiuy.operator.core.aop;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.model.MyLog;
import com.jiuy.rb.service.common.ILogRbService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志拦截器
 * @author Aison
 * @version V1.0
 * @date 2018/6/5 23:09
 * @Copyright 玖远网络
 */
@Aspect
@Component
public class LogAopRb {



    @Autowired
    private ILogRbService logsService;

    /**
     * 定义切面
     * @author Aison
     * @date 2018/6/5 23:12
     */
    @Pointcut(value = "@annotation(com.jiuy.base.annotation.MyLogs)")
    public void mylog() {

    }
    /**
     * 切面mylog
     * @param pjp 切点对象
     * @author Aison
     * @date 2018/6/5 23:12
     */
    @Around("mylog()")
    public Object invoke(ProceedingJoinPoint pjp) throws Throwable {

        MyLog log = (MyLog) pjp.proceed();
        if (log != null) {
            Signature signature =  pjp.getSignature();
            Method method = ((MethodSignature) signature).getMethod();
            Method realMethod = pjp.getTarget().getClass().getDeclaredMethod(signature.getName(),method.getParameterTypes());
            MyLogs logs = realMethod.getAnnotation(MyLogs.class);
            log.setMethod(method.toString().substring(6));
            log.setModel(logs.model());

            logsService.syncOperatorLogs(log);

        }
        return log;
    }
}

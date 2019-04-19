package com.jiuy.base.Interceptor;

import com.jiuy.base.annotation.MyLogs;
import com.jiuy.base.model.MyLog;
import com.jiuy.service.ILogsService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志拦截器
 * @author Aison
 * @version V1.0
 * @date 2018/6/5 23:09
 * @Copyright 玖远网络
 */
@Aspect
@Component
public class MylogAop {

    private final ILogsService logsService;

    @Autowired
    public MylogAop(ILogsService logsService) {
        this.logsService = logsService;
    }

    /**
     * 定义切面
     * @author Aison
     * @date 2018/6/5 23:12
     */
    @Pointcut("@annotation(com.jiuy.base.annotation.MyLogs)")
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
            MethodSignature m = (MethodSignature) pjp.getSignature();
            MyLogs logs = m.getMethod().getAnnotation(MyLogs.class);
            log.setMethod(m.toString().substring(6));
            log.setModel(logs.model());
            logsService.syncAcceptLogs(log);
        }
        return log;
    }
}

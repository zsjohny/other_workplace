/**
 * 
 */
package com.yujj.util.asyn;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;

import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.asyn.StackTraceUtil;
import com.jiuyuan.util.asyn.annotation.NeedSyncExecution;
import com.yujj.util.asyn.annotation.AsynExecutable;

/**
 * @author LWS
 */
@Aspect
public class AsynchronizationWatcher {

    public MessageAsynService getMessageAsynService() {
        return messageAsynService;
    }

    public void setMessageAsynService(MessageAsynService messageAsynService) {
        this.messageAsynService = messageAsynService;
    }

    @Around("execution(* com.yujj.business..*.*(..)) || " + "@annotation(com.yujj.util.asyn.annotation.AsynExecutable)")
    public Object invoke(final ProceedingJoinPoint pjp) throws Throwable {
        String typeName = pjp.getSignature().getDeclaringTypeName();
        String name = pjp.getSignature().getName();
        String marker = typeName + _DELIMETER + name;
        try {
            // logger.debug("set method signature:" + pjp);
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            AsynExecutable aeAnotation = AnnotationUtils.findAnnotation(method, AsynExecutable.class);
            if (aeAnotation != null && !StackTraceUtil.containParentCallStack(NeedSyncExecution.class)) {
                if (aeAnotation.bRun()) {
                    logger.info("setting up asynchronous environment for : " + marker);
                    String cachedKey = getCachedKey(marker);
                    messageAsynService.asynExecuteMethod(cachedKey);
                    memcachedService.add(MemcachedKey.GROUP_KEY_METHOD_INVOCATION, cachedKey, _EXPIRE_TIME,
                        pjp.getArgs());
                    return _SUCCESS;
                } else {
                    return _FAIL;
                }
            } else {
                return pjp.proceed();
            }
        } finally {

        }
    }

    private String getCachedKey(String marker) {
        long nanoTime = System.nanoTime();
        return marker + _DELIMETER + nanoTime;
    }

    @Autowired
    private MessageAsynService messageAsynService;

    @Autowired
    private MemcachedService memcachedService;

    private final Integer _SUCCESS = new Integer(1);

    private final Integer _FAIL = new Integer(0);

    private final String _DELIMETER = "#";

    private final int _EXPIRE_TIME = 60 * 5; // 5 minutes

    private static final Logger logger = Logger.getLogger(AsynchronizationWatcher.class);
}

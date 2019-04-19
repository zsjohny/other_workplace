/**
 * 
 */
package com.yujj.util.asyn;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
/*
 * import org.springframework.util.StringUtils; import org.apache.commons.lang3.reflect.MethodUtils; import
 * java.lang.reflect.InvocationTargetException;
 */

import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.asyn.annotation.NeedSyncExecution;

/**
 * @author LWS
 */
@NeedSyncExecution
public class AsynchronizationExecutor implements ApplicationContextAware {

    public void execute(String methodSignature) {
        Object[] cachedPjpArgs = getMethodArg(methodSignature);
        invokeMethod(methodSignature,cachedPjpArgs);
        afterMethodInvocation(methodSignature);
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        applicationContext = arg0;
    }

    private ApplicationContext applicationContext;

    private String _SIGNATURE_DELIMITER = "#";

    @Autowired
    private MemcachedService memcachedService;

    private Object[] getMethodArg(String methodSignature) {
        Object[] args =  (Object[]) memcachedService.get(MemcachedKey.GROUP_KEY_METHOD_INVOCATION, methodSignature);
        return args;
    }

    private Object invokeMethod(String methodSignature, Object[] args){
        if(null != methodSignature && methodSignature.contains(_SIGNATURE_DELIMITER)){
            String[] methodPart = null;
            methodPart = StringUtils.split(methodSignature, _SIGNATURE_DELIMITER);
            Class<?> beanClazz = null;
            try {
                beanClazz = Class.forName(methodPart[0]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Object beanObj = applicationContext.getBean(beanClazz);
            String methodName = methodPart[1];
            try {
                logger.info("execute method asynchronous : " + methodSignature);
                Object ret = MethodUtils.invokeExactMethod(beanObj, methodName,args);
                return ret;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } finally{
                
            }
            return null;
        }else{
            return null;
        }
    }
    
    private void afterMethodInvocation(String methodSignature){
        memcachedService.remove(MemcachedKey.GROUP_KEY_METHOD_INVOCATION,methodSignature);
    }
    
    private static final Logger logger = Logger.getLogger(AsynchronizationExecutor.class);
}

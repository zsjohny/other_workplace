package com.jiuyuan.util.asyn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义必须被同步执行的方法，该注解优先级高于AsynExecutable
 * 
 * @author LWS
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface NeedSyncExecution {

}

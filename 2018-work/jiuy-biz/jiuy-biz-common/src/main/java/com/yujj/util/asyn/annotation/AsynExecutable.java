package com.yujj.util.asyn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义可被异步执行的方法
 * 
 * @author LWS
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AsynExecutable {
    /**
     * 是否继续执行
     * @return
     */
    public boolean bRun() default true;
}

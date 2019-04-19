package com.yujj.web.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IpConfine {
    /**
     * ip列表
     */
    String[] value() default {};
    
    /**
     * 黑名单模式，默认为白名单模式
     */
    boolean blackMode() default false;
}

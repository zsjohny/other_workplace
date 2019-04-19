package com.jiuy.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 是否需要登陆
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/27 9:52
 * @Copyright 玖远网络
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Login {
}

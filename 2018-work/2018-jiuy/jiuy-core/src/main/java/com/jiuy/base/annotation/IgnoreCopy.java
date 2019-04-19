package com.jiuy.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TOTO
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/31 17:15
 * @Copyright 玖远网络
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IgnoreCopy {

}

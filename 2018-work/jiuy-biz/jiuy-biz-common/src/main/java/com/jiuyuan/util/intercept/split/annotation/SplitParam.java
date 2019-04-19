package com.jiuyuan.util.intercept.split.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate a Collection argument, to tell the interceptor that the collection needs to be splitted when its
 * size is too large (and then the method will be executed for multiple times). You should use at most 1 @SplitParam for
 * a single method. If it's not the case, then only the first one will be functioning.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface SplitParam {

    int size() default 1000;
}

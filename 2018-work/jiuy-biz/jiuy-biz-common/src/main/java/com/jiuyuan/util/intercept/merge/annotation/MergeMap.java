package com.jiuyuan.util.intercept.merge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.merge.handler.MergeMapHandler;
import com.jiuyuan.util.merger.MapMerger;
import com.jiuyuan.util.merger.ObjectMerger;
import com.yujj.util.intercept.Interceptable;

/**
 * Merge multiple map objects as one.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Interceptable
@MergeCapable(handler = MergeMapHandler.class)
public @interface MergeMap {

    Class<? extends ObjectMerger> resultMerger() default MapMerger.class;
}

package com.jiuyuan.util.intercept.merge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.merge.handler.MergeNumberHandler;
import com.jiuyuan.util.merger.ObjectMerger;
import com.jiuyuan.util.merger.SumMerger;
import com.yujj.util.intercept.Interceptable;

/**
 * 'Merge' multiple numbers as one. (sum, max, min, etc)
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Interceptable
@MergeCapable(handler = MergeNumberHandler.class)
public @interface MergeNumber {

    Class<? extends ObjectMerger> resultMerger() default SumMerger.class;
}

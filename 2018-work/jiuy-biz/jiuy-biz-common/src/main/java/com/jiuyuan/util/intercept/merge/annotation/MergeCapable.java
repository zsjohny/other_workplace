package com.jiuyuan.util.intercept.merge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.merge.handler.MergeCapableHandler;


/**
 * When a method is splitted and executed for multiple times, the results need to be merged. The MergeCapable-annotated
 * annotations are capable of merging a collection of result values.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface MergeCapable {

    Class<? extends MergeCapableHandler> handler();
}

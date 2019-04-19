package com.jiuyuan.util.intercept.merge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.merge.handler.MergeCollectionHandler;
import com.jiuyuan.util.merger.CollectionMerger;
import com.jiuyuan.util.merger.ObjectMerger;
import com.yujj.util.intercept.Interceptable;

/**
 * Merge multiple collection objects as one. Be noted that by default the merged collection will be orderless. You can
 * set the orderBy field to sort the result collection in memory.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Interceptable
@MergeCapable(handler = MergeCollectionHandler.class)
public @interface MergeCollection {

    Class<? extends ObjectMerger> resultMerger() default CollectionMerger.class;

    String[] uniKeys() default {};

    String order() default ""; // eg. "+id -age" or "+"
}

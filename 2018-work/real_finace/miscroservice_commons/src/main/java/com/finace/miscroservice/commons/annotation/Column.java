package com.finace.miscroservice.commons.annotation;


import com.finace.miscroservice.commons.handler.DbHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表格的长度值
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    /**
     * 长度
     *
     * @return
     */
    int value() default 0;


    /**
     * 精度
     *
     * @return
     */
    int precision() default 0;


    /**
     * 注释说明
     *
     * @return
     */
    String commit() default "";

    /**
     * 默认值的添加
     *
     * @return
     */
    String defaultVal() default "";

    /**
     * 是否允许null true 是null false 不是null
     *
     * @return
     */
    boolean isNUll() default true;


    /**
     * 日期的策略
     *
     * @return
     */
    DbHandler.DateGeneStrategy dateGeneStrategy() default DbHandler.DateGeneStrategy.DEFAULT;

}

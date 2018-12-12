package com.finace.miscroservice.commons.annotation;

import com.finace.miscroservice.commons.enums.InterceptorModeEnum;
import com.finace.miscroservice.commons.utils.JwtToken;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 频率验证的开启
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateVerify {
    /**
     * 限定的次数
     *
     * @return
     */
    int value() default 10;


    /**
     * 限定的时间
     *
     * @return
     */
    int time() default 5;


    /**
     * 限定的时间单位
     *
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;


    /**
     * 拦截验证的字段设置
     */
    String interceptingField() default JwtToken.UID;


    /**
     * 是否允许跨域
     */
    boolean allowedCrossDomain() default true;

    /**
     * 拦截方式
     *
     * @return
     */
    InterceptorModeEnum interceptingMode() default InterceptorModeEnum.HEADER;


}

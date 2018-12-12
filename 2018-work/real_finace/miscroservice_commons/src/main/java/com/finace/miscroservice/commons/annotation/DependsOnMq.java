package com.finace.miscroservice.commons.annotation;

import org.springframework.context.annotation.DependsOn;

import java.lang.annotation.*;

import static com.finace.miscroservice.commons.utils.BeanNameConstant.MQ_MANAGER_DISTRIBUTE_BEAN_NAME;

/**
 * 依赖Mq的注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DependsOn(MQ_MANAGER_DISTRIBUTE_BEAN_NAME)
public @interface DependsOnMq {
}

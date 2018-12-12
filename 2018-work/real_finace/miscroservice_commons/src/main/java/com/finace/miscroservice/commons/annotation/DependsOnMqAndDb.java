package com.finace.miscroservice.commons.annotation;

import org.springframework.context.annotation.DependsOn;

import java.lang.annotation.*;

import static com.finace.miscroservice.commons.utils.BeanNameConstant.FLYWAY_BEAN_NAME;
import static com.finace.miscroservice.commons.utils.BeanNameConstant.MQ_MANAGER_DISTRIBUTE_BEAN_NAME;

/**
 * 依赖Mq和db的注解
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DependsOn({FLYWAY_BEAN_NAME, MQ_MANAGER_DISTRIBUTE_BEAN_NAME})
public @interface DependsOnMqAndDb {
}

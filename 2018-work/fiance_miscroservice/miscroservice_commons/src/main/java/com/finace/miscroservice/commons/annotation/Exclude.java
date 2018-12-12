package com.finace.miscroservice.commons.annotation;

import java.lang.annotation.*;

/**
 * 排除不想被扫描的注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Exclude {
}

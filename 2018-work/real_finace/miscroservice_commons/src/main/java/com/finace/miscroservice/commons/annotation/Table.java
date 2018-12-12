package com.finace.miscroservice.commons.annotation;

import java.lang.annotation.*;

/**
 * 建表标识
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {
    String value();

}

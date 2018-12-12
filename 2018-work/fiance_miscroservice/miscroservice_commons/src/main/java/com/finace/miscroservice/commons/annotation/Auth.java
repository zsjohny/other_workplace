package com.finace.miscroservice.commons.annotation;

import java.lang.annotation.*;

/**
 * 系统权限认证的标识
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auth {
}

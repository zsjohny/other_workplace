/**
 * use for control the version of the product and 
 * transformations between
 */
package com.jiuyuan.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author LWS
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface VersionControl {
    String value() default "0.0.0.0";
}

package com.jiuy.core.util.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to instruct the interceptor that a method is interceptable. It's a meta-annotation, which means that the
 * Interceptable-annotated annotations have the equal functionalities.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD })
@Interceptable
public @interface Interceptable {
    //
}

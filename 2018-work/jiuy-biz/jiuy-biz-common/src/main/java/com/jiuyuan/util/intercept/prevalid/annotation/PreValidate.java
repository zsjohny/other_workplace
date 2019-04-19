package com.jiuyuan.util.intercept.prevalid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.prevalid.validator.PreValidator;


/**
 * Used to annotate another annotation so that the interceptor knows that the annotated argument needs pre-validation.
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface PreValidate {

    Class<? extends PreValidator> by();
}

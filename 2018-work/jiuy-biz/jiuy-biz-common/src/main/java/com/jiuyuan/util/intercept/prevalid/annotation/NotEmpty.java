package com.jiuyuan.util.intercept.prevalid.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.intercept.prevalid.validator.NotEmptyValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@PreValidate(by = NotEmptyValidator.class)
public @interface NotEmpty {

    /**
     * @return name of the property that this annotation is applied to.
     */
    String property() default "";

    /**
     * @return whether to throw an exceptioin when pre-validation fails. If not, a default value should be returned for
     *         the method.
     */
    boolean exception() default false;
}

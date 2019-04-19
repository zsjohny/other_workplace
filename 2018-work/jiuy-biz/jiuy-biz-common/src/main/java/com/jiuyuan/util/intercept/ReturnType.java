package com.jiuyuan.util.intercept;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jiuyuan.util.instantiate.DefaultInstanceFactory;
import com.jiuyuan.util.instantiate.InstanceFactory;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ReturnType {

    Class<?> type() default void.class;

    /**
     * The factory used to instanciate this type.
     */
    Class<? extends InstanceFactory> factory() default DefaultInstanceFactory.class;

    /**
     * A legal value that can be used to infect the instantiation process.
     */
    String hint() default "";
}

package com.test.config;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
// 指定真正实现校验规则的类
@Constraint(validatedBy = PhoneValidationValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PhoneValidation {
    String message() default "不是正确的手机号码";


    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
package com.songxm.credit.compnent;

import com.songxm.credit.comon.credit.diversion.enums.AuthQRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthQScope {
    AuthQRole[] value() default {AuthQRole.QIU_SERVICE};
}

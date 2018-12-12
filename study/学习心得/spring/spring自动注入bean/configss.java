package com.e_commerce.miscroservice.authorize.config;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(configs.class)
public @interface configss {
}

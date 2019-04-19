package com.jiuyuan.ext.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.ibatis.type.TypeHandler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface RegisterTypeHandler {

    @SuppressWarnings("rawtypes")
    Class<? extends TypeHandler> value();
}

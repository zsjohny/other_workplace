package com.jiuyuan.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE,ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLogin {
    /**
     * <pre>
     * 只能作用于requestmapping方法，可用于逃脱登录校验（如一个controller里面的大部分请求需要登录校验，某些方法又不需要登录校验）
     * 这种情况可以在controller标记@Login，然后对不需登录校验的方法标记@NoLogin
     * </pre>
     */
}

package com.jiuyuan.util.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FinancialPermisson {
    /***
     * <pre>
     *  可标记于controller类或方法，标记在类上时代表controller下面的所有requestmapping方法都需登录校验，
     *  仅标记在方法上时表示该方法需进行登录校验
     * </pre>
     */
}

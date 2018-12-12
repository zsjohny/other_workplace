package com.onway.baib.core.annotation.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对象根据这个注解，在反射时，能获取对应的信息，可以根据注解信息来做校验
 * 
 * @author wwf
 * @version $Id: FieldMeta.java, v 0.1 2016年8月1日 上午11:55:47 wwf Exp $
 */
@Retention(RetentionPolicy.RUNTIME)
// 注解会在class字节码文件中存在，在运行时可以通过反射获取到  
@Target({ ElementType.FIELD, ElementType.METHOD })
//定义注解的作用目标**作用范围字段、枚举的常量/方法  
@Documented
//说明该注解将被包含在javadoc中 
public @interface FieldMeta {

    /** 
     * 字段是否可以为空判断
     * @return true:表示不能为空，false:表示可以为空
     */
    boolean notnull() default false;

    /** 
     * 字段描述 
     * @return 
     */
    String description() default "";

    /**
     * 字段最大的长度
     * 
     * @return
     */
    int maxlength() default 32;

    /**
     * 默认值
     * 
     * @return
     */
    String defaultVal() default "";

    /**
     * 该参数对应模型类的字段
     * 
     * @return
     */
    String modelField() default "";

    /**
     * 枚举类名【这里针对枚举类】
     */
    String returnClass() default "";

    /**
     * 是否在前台列表中展示
     * 
     * @return true 展示 ;false 不展示
     */
    boolean showOrNot() default false;

    /**
     * 是否在前台列表中展示
     * 
     * @return true 展示 ;false 不展示
     */
    boolean formShowOrNot() default true;

    /**
     * 参数在网页中的类型
     * 
     * @return
     */
    String dataWebType() default "text";
}

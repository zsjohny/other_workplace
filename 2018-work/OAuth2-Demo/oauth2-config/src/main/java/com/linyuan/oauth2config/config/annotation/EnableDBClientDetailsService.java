package com.linyuan.oauth2config.config.annotation;

import com.linyuan.oauth2config.config.service.DBClientDetailsService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: 林塬
 * @date: 2018/1/22
 * @description: 在启动类上添加该注解来----开启从数据库加载客户端详情
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBClientDetailsService.class)
public @interface EnableDBClientDetailsService {
}

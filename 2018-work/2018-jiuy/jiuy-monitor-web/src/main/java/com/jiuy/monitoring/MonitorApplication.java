package com.jiuy.monitoring;

import com.jiuy.monitoring.config.ReWriterFilter;
import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.Resource;

/**
 * 统计入口类
 * @version 1.1
 * @author Aison
 * @date 2018/5/19 20:40
 * @Copyright 玖远网络
 */
@EnableAsync
@SpringBootApplication
@ComponentScan("com.jiuy")
@MapperScan("com.jiuy.**.mapper")
@Log4j2
public class MonitorApplication {

   public static void main(String[] args) {
       SpringApplication.run(MonitorApplication.class, args);
   }



    @Resource(name="reWriterFilter")
    @Qualifier("reWriterFilter")
    private ReWriterFilter reWriterFilter;

    /**
     * 注册一个过滤器 名字叫 reWriterFilter
     * 之所以注册一个是因为要在 这个拦截器里面注入bean
     * 不这样做注入不进去
     * @author Aison
     * @date 2018/6/8 11:43
     */
    @Bean("reWriterFilter")
    public ReWriterFilter reWriterFilter() {
        return new ReWriterFilter();
    }

    /**
     * 添加一个拦截器拦截所有的请求
     * @author Aison
     * @date 2018/6/8 11:44
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        return new FilterRegistrationBean(reWriterFilter);
    }
}

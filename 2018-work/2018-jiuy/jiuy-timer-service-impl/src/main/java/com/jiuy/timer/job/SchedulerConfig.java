package com.jiuy.timer.job;


import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;


/**
 * 定时器的配置
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/28 15:30
 * @Copyright 玖远网络
 */
@Configuration
public class SchedulerConfig {

    @Resource(name="dataSource")
    private DataSource dataSource;
    @Resource(name = "jobFactory")
    private JobFactory jobFactory;

    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(quartzProperties());
        factory.setJobFactory(jobFactory);
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * quartz初始化监听器
     * @author Aison
     * @date 2018/5/28 15:31
     */
    @Bean
    public QuartzInitializerListener executorListener() {

        return new QuartzInitializerListener();
    }

    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     * @author Aison
     * @date 2018/5/28 15:31
     */
    @Bean(name="Scheduler")
    public Scheduler scheduler() throws IOException {

        return schedulerFactoryBean().getScheduler();
    }

}

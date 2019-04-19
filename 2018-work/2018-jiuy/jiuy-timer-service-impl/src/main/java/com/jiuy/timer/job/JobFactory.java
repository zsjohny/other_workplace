package com.jiuy.timer.job;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 *  quartz的job工厂类解决job中不能注入spring bean 问题
 *
 * @author Aison
 * @version V1.0
 * @date 2018/5/29 13:36
 * @Copyright 玖远网络
 */
@Configuration
@AutoConfigureBefore(SchedulerConfig.class)
public class JobFactory extends AdaptableJobFactory {


    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }

    @Bean(name="jbFactory")
    public JobFactory getFactory(){
        return new JobFactory();
    }
}

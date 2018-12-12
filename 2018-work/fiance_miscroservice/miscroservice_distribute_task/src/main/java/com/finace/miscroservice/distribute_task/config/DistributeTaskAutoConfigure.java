package com.finace.miscroservice.distribute_task.config;

import com.finace.miscroservice.distribute_task.helper.TimeTaskHelper;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskJob;
import com.finace.miscroservice.distribute_task.util.IpUtil;
import com.finace.miscroservice.distribute_task.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(TimeTaskHelper.class)
@EnableConfigurationProperties(DistributeTaskConfigure.class)
public class DistributeTaskAutoConfigure {

    @Autowired
    private DistributeTaskConfigure distributeTaskConfigure;


    @Bean
    @ConditionalOnProperty(prefix = "distribute.task", value = "enabled", havingValue = "true")
    public TimeTaskHelper createTimeTaskHelper() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        return new TimeTaskHelper(distributeTaskConfigure.getServerList(), (TimeTaskJob) Class.forName(distributeTaskConfigure.getTimeTaskJobName()).newInstance(),
                (LogUtil) Class.forName(distributeTaskConfigure.getLogUtilName()).newInstance()
                , (IpUtil) Class.forName(distributeTaskConfigure.getIpUtilName()).newInstance());

    }


}

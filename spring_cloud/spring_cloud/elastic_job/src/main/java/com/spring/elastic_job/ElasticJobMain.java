package com.spring.elastic_job;


import com.spring.elastic_job.task.JobTypeEnum;
import com.spring.elastic_job.task.SimpleJobBuild;
import com.spring.elastic_job.task.TaskJobDemo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

import static com.spring.elastic_job.task.JobParamsConfig.getJobName;
import static com.spring.elastic_job.task.JobParamsConfig.getNameSpace;

@SpringBootApplication
public class ElasticJobMain {
    @Value("${zookeeper.serverList}")
    private String ZKServerLists;

    @PostConstruct
    public void init() {

        new SimpleJobBuild().start(ZKServerLists,
                getNameSpace(), TaskJobDemo.class, getJobName(),
                "0/20 * * * * ?",
                JobTypeEnum.TEST.toJobName(), null, "a", "b");
    }

    public static void main(String[] args) {
        SpringApplication.run(ElasticJobMain.class, args);
    }
}

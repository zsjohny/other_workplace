package com.spring.elastic_job.task;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 执行工作所需参数的常规配置
 */
public class JobParamsConfig {

    private static final AtomicInteger JOB_VERSION = new AtomicInteger();

    private static final String LINK = "_";

    private static final String COMPANY_NAME = "yiTongJin";

    private static final String JOB_NAME = COMPANY_NAME + LINK + "jobTaskName" + LINK + System.currentTimeMillis() + LINK;

    /**
     * 获得任务名称
     *
     * @return
     */
    public static String getJobName() {
        return JobParamsConfig.JOB_NAME + JOB_VERSION.getAndIncrement();
    }

    /**
     * 获得空间名称
     *
     * @return
     */
    public static String getNameSpace() {
        return COMPANY_NAME + LINK + "zookeeperNameSpace";
    }


    public static void main(String[] args) {
        System.out.println(getJobName());
        System.out.println(getJobName());
        System.out.println(getJobName());
    }

}

package com.spring.elastic_job.task;

/**
 * 工作的类型 枚举
 */
public enum JobTypeEnum {

    TEST("test");
    private String jobName;

    JobTypeEnum(String jobName) {
        this.jobName = jobName;
    }

    /**
     * 获取job的名称
     * @return
     */
    public String toJobName() {
        return jobName;
    }

}

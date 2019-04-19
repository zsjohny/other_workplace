package com.jiuyuan.builder;

import com.util.JobDetailVo;


/**
 * 构建 JobDetailVo
 *
 * <p>
 *     屏蔽不需要设置setter
 * @author Charlie(唐静)
 * @version V1.0
 * @title 构建 JobDetailVo
 * @package jiuy-biz
 * @description
 * @date 2018/6/7 18:29
 * @copyright 玖远网络
 */
public class JobRequestBuilder{

    private JobDetailVo vo = new JobDetailVo();

    /**
     * 任务名称
     */
    public JobRequestBuilder setJobName(String jobName) {
        vo.setJobName(jobName);
        return this;
    }

    /**
     * 任务分组名称
     */
    public JobRequestBuilder setJobGroupName(String jobGroupName) {
        vo.setJobGroup(jobGroupName);
        return this;
    }

    /**
     * 任务时间规则
     */
    public JobRequestBuilder setCronExpression(String cronExpression) {
        vo.setCronExpression(cronExpression);
        return this;
    }

    /**
     *任务描述
     */
    public JobRequestBuilder setJobComment(String jobComment) {
        vo.setJobComment(jobComment);
        return this;
    }

    public JobDetailVo build() {
        return vo;
    }

    /**
     * Job 回调服务
     */
    public JobRequestBuilder setCallbackServer(JobCallbackUrl jobCallback) {
        vo.setFeedbackData(jobCallback.getFeedbackData());
        vo.setFeedbackUrl(jobCallback.getFeedbackUrl());
        return this;
    }
}

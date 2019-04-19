package com.util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title 发送定时任务VO
 * @package jiuy-biz
 * @description
 * @date 2018/5/30 12:59
 * @copyright 玖远网络
 */
public class JobDetailVo{


    /**
     * 任务名称
     **/
    private String jobName;
    /**
     * 任务时间 什么时候执行
     */
    private String  data;
    /**
     * 回调地址
     **/
    private String  feedbackUrl;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 任务时间规则
     */
    private String cronExpression;
    /**
     * 操作类型 1: 新增
     */
    private Integer jobType;
    /**
     * 回调参数 {name:aison,age:30,sex:'男'}
     */
    private String feedbackData;
    /**
     *任务描述
     **/
    private String jobComment;


    public List<NameValuePair> getNameValueList() {
        ArrayList<NameValuePair> list = new ArrayList<>(8);
        list.add(new BasicNameValuePair("jobName", getJobName()));
        list.add(new BasicNameValuePair("jobGroup", getJobGroup()));
        list.add(new BasicNameValuePair("data", getData()));
        list.add(new BasicNameValuePair("feedbackUrl", getFeedbackUrl()));
        list.add(new BasicNameValuePair("cronExpression", getCronExpression()));
        list.add(new BasicNameValuePair("jobType", String.valueOf(getJobType())));
        list.add(new BasicNameValuePair("feedbackData", getFeedbackData()));
        list.add(new BasicNameValuePair("jobComment", getJobComment()));
        return list;
    }



    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getFeedbackData() {
        return feedbackData;
    }

    public void setFeedbackData(String feedbackData) {
        this.feedbackData = feedbackData;
    }

    public String getJobComment() {
        return jobComment;
    }

    public void setJobComment(String jobComment) {
        this.jobComment = jobComment;
    }

    @Override
    public String toString() {
        return "JobDetailVo{" +
                "jobName='" + jobName + '\'' +
                ", data='" + data + '\'' +
                ", feedbackUrl='" + feedbackUrl + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", jobType=" + jobType +
                ", feedbackData='" + feedbackData + '\'' +
                ", jobComment='" + jobComment + '\'' +
                '}';
    }

}
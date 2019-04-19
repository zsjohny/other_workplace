package com.jiuyuan.builder;

/**
 * @author Charlie(唐静)
 * @version V1.0
 * @title job服务回调 服务地址+请求参数
 * @package jiuy-biz
 * @description
 * @date 2018/6/7 18:46
 * @copyright 玖远网络
 */
public class JobCallbackUrl{

    /**
     * 回调参数 {name:aison,age:30,sex:'男'}
     */
    private String feedbackData;
    /**
     * 回调地址
     **/
    private String  feedbackUrl;

    public JobCallbackUrl(String feedbackUrl, String feedbackData) {
        this.feedbackData = feedbackData;
        this.feedbackUrl = feedbackUrl;
    }

    public String getFeedbackData() {
        return feedbackData;
    }

    public void setFeedbackData(String feedbackData) {
        this.feedbackData = feedbackData;
    }

    public String getFeedbackUrl() {
        return feedbackUrl;
    }

    public void setFeedbackUrl(String feedbackUrl) {
        this.feedbackUrl = feedbackUrl;
    }
}

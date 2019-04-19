package com.jiuyuan.util.oauth.sns.common.response;

/**
 * 响应
 */
public interface ISnsResponse<T> {

    String getUri();

    int getStatusCode();

    boolean isStatusCodeFine();

    String getResponseText();

    SnsResponseType getResponseType();

    String getTextMessage();

    T getData();

    void setData(T data);
}
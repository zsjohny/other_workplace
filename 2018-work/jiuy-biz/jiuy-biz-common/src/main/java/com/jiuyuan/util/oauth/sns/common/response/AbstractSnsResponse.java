package com.jiuyuan.util.oauth.sns.common.response;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.jiuyuan.util.http.component.CachedHttpResponse;


public abstract class AbstractSnsResponse<T> implements ISnsResponse<T> {

    private CachedHttpResponse response;

    private T data;

    private SnsResponseType responseType;

    private String textMessage;

    public AbstractSnsResponse(CachedHttpResponse response) {
        this.response = response;
        this.responseType = parseResponse();

        if (StringUtils.isBlank(textMessage) && responseType != null) {
            textMessage = responseType.getMessage();
        }
    }

    public AbstractSnsResponse(AbstractSnsResponse<?> response, T data) {
        this.response = response.response;
        this.data = data;
        this.responseType = response.responseType;
        this.textMessage = response.textMessage;
    }

    @Override
    public String getUri() {
        return this.response.getUri();
    }

    @Override
    public int getStatusCode() {
        return this.response.getStatusCode();
    }

    @Override
    public boolean isStatusCodeFine() {
        return this.response.isStatusCodeFine();
    }

    @Override
    public String getResponseText() {
        return this.response.getResponseText();
    }

    @Override
    public SnsResponseType getResponseType() {
        return this.responseType;
    }

    @Override
    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String message) {
        this.textMessage = message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }

    protected abstract SnsResponseType parseResponse();

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("uri", getUri()).append("statusCode",
            getStatusCode()).append("responseType", getResponseType()).append("message", getTextMessage()).append(
            "responseText", getResponseText()).build();
    }
}

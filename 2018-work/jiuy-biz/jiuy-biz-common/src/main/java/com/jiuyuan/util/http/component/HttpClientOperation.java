package com.jiuyuan.util.http.component;

import com.jiuyuan.util.http.component.CachedHttpResponse;
import com.jiuyuan.util.http.log.LogInitializer;

public abstract class HttpClientOperation implements LogInitializer {

    private String operationName;

    public HttpClientOperation(String operationName) {
        this.operationName = operationName;
    }

    public abstract CachedHttpResponse sendRequest() throws Exception;

    public String getOperationName() {
        return operationName;
    }
}

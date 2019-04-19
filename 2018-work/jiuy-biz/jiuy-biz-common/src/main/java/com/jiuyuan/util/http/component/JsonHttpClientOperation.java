package com.jiuyuan.util.http.component;

import com.jiuyuan.util.http.log.LogInitializer;

public abstract class JsonHttpClientOperation implements LogInitializer {

    private String operationName;

    public JsonHttpClientOperation(String operationName) {
        this.operationName = operationName;
    }

    public abstract JsonHttpResponse sendRequest() throws Exception;

    public String getOperationName() {
        return operationName;
    }
}

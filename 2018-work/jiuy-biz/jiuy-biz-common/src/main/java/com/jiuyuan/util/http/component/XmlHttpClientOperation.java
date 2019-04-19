package com.jiuyuan.util.http.component;

import com.jiuyuan.util.http.log.LogInitializer;

public abstract class XmlHttpClientOperation implements LogInitializer {

    private String operationName;

    public XmlHttpClientOperation(String operationName) {
        this.operationName = operationName;
    }

    public abstract XmlHttpResponse sendRequest() throws Exception;

    public String getOperationName() {
        return operationName;
    }
}

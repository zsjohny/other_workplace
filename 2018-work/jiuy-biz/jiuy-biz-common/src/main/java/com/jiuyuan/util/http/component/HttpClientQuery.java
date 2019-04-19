package com.jiuyuan.util.http.component;

import com.jiuyuan.util.http.component.HttpClientOperation;
import com.jiuyuan.util.http.log.LogBuilder;

public abstract class HttpClientQuery extends HttpClientOperation {

    public HttpClientQuery(String operationName) {
        super(operationName);
    }

    public abstract boolean readResponse(String responseText, LogBuilder errorLog);
}

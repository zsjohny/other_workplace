package com.jiuyuan.util.http.component;

import com.jiuyuan.util.http.Dom4jElementDecorator;
import com.jiuyuan.util.http.log.LogBuilder;

public abstract class XmlHttpClientQuery extends XmlHttpClientOperation {

    public XmlHttpClientQuery(String operationName) {
        super(operationName);
    }

    public abstract boolean readResponse(Dom4jElementDecorator root, LogBuilder errorLog);
}

package com.jiuyuan.util.http.component;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.util.http.log.LogBuilder;

public abstract class JsonHttpClientQuery extends JsonHttpClientOperation {

    public JsonHttpClientQuery(String operationName) {
        super(operationName);
    }

    public abstract boolean readResponse(JSONObject jsonObject, LogBuilder errorLog);
}

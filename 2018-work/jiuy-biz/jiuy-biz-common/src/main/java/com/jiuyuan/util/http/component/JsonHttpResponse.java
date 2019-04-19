package com.jiuyuan.util.http.component;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonHttpResponse extends CachedHttpResponse {

    private JSONObject jsonObject;

    public JsonHttpResponse(String uri, int statusCode, byte[] body, String charset, Header[] headers) {
        super(uri, statusCode, body, charset, headers);
    }

    public JsonHttpResponse(CachedHttpResponse httpResponse) {
        this(httpResponse.getUri(), httpResponse.getStatusCode(), httpResponse.getBody(), httpResponse.getCharset(),
            httpResponse.getHeaders());
    }

    public JSONObject parseJson() {
        if (jsonObject == null) {
            jsonObject = JSON.parseObject(getResponseText());
        }
        return jsonObject;
    }

}
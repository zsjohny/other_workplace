package com.jiuyuan.util.oauth.v2;

import com.jiuyuan.util.http.httpclient.HttpParamsX;

public class V2Config {

    private String authorizeScope;

    private String authorizeUri;

    private String accessTokenUri;

    private String defaultCharset;

    private HttpParamsX defaultHttpParams;

    public String getAuthorizeScope() {
        return authorizeScope;
    }

    public void setAuthorizeScope(String authorizeScope) {
        this.authorizeScope = authorizeScope;
    }

    public String getAuthorizeUri() {
        return authorizeUri;
    }

    public void setAuthorizeUri(String authorizeUri) {
        this.authorizeUri = authorizeUri;
    }

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }

    public HttpParamsX getDefaultHttpParams() {
        return defaultHttpParams;
    }

    public void setDefaultHttpParams(HttpParamsX defaultHttpParams) {
        this.defaultHttpParams = defaultHttpParams;
    }
}

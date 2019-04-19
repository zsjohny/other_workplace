package com.yujj.constant;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.util.enumeration.IntEnum;
//import com.jiuyuan.web.interceptor.UriBuilder;
import com.yujj.util.uri.UriBuilder;

public enum TokenUrlType implements IntEnum {
    SIGN_URL(1, "/static/app/signup.html");
    
    private TokenUrlType(int intValue, String url) {
        this.intValue = intValue;
        this.url = url;
    }

    private int intValue;
    
    private String url;
    
    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getUrl() {
        return url;
    }

    public String genUrl(String token) {
        UriBuilder builder = new UriBuilder(Constants.SERVER_URL_HTTPS + "/mobile/login/token.do");
        builder.set("token", token);
        builder.set("target_url", this.getUrl());
        return builder.toUri();
    }

}

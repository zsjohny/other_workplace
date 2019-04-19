package com.jiuyuan.constant;

import com.jiuyuan.util.spring.ControllerUtil;

public enum OAuthCallbackModule {
    
    LOGIN_REDIRECT("login_redirect", ControllerUtil.forward("/m/login/ext/oauth.do"));
    
    private OAuthCallbackModule(String module, String url) {
        this.module = module;
        this.url = url;
    }
    
    private String module;
    
    private String url;

    public String getModule() {
        return module;
    }

    public String getUrl() {
        return url;
    }
    
    public static String getUrl(String module) {
        for(OAuthCallbackModule authModule : values()) {
            if(authModule.module.equals(module)) {
                return authModule.url;
            }
        }
        throw new IllegalArgumentException("unsupport module");
    }

}


package com.jiuyuan.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.ClientPlatform;

public class ClientPlatformMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

    public ClientPlatformMethodArgumentResolver() {
        super(Constants.KEY_CLIENT_PLATFORM, ClientPlatform.class);
    }
}

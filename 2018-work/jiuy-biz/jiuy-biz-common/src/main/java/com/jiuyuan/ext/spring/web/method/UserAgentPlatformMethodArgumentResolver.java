package com.jiuyuan.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.Platform;

public class UserAgentPlatformMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

    public UserAgentPlatformMethodArgumentResolver() {
        super(Constants.KEY_USER_AGENT_PLATFORM, Platform.class);
    }
}

package com.jiuyuan.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.UserDetail;

public class UserDetailMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

    public UserDetailMethodArgumentResolver() {
        super(Constants.KEY_USER_DETAIL, UserDetail.class);
    }
}

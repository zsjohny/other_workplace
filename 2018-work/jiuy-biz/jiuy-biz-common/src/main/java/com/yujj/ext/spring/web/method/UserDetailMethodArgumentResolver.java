package com.yujj.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.ext.spring.web.method.RequestAttributeBasedMethodArgumentResolver;
import com.yujj.entity.account.UserDetail;

public class UserDetailMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

    public UserDetailMethodArgumentResolver() {
        super(Constants.KEY_USER_DETAIL, UserDetail.class);
    }
}

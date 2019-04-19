package com.wxa.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.ext.spring.web.method.RequestAttributeBasedMethodArgumentResolver;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;

public class MemberDetailMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

    public MemberDetailMethodArgumentResolver() {
        super(Constants.KEY_WXA_MEMBER_DETAIL, MemberDetail.class);
    }
}

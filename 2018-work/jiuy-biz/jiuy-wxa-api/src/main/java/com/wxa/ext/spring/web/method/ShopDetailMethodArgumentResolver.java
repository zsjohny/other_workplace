package com.wxa.ext.spring.web.method;

import com.jiuyuan.constant.Constants;
import com.jiuyuan.ext.spring.web.method.RequestAttributeBasedMethodArgumentResolver;
import com.store.entity.ShopDetail;

public class ShopDetailMethodArgumentResolver extends RequestAttributeBasedMethodArgumentResolver {

	public ShopDetailMethodArgumentResolver() {
        super(Constants.KEY_WXA_SHOP_DETAIL, ShopDetail.class);
    }
}

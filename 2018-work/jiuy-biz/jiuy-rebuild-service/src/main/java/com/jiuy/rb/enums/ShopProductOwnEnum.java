package com.jiuy.rb.enums;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/3 18:57
 * @Copyright 玖远网络
 */
public enum ShopProductOwnEnum{



    /**
     * 0平台供应商商品
     */
    SUPPLIER(0),
    /**
     * 1是用户自定义款
     */
    SELF_CUSTOM(1),
    /**
     * 2 用户自营平台同款
     */
    SELF_SAMPLE_STYLE(2);

    private int code;

    ShopProductOwnEnum(int code) {
        this.code = code;
    }

    public boolean isThis(Integer code) {
        if (null == code) {
            return false;
        }

        return this.code == code;
    }

    public int getCode() {
        return code;
    }
}

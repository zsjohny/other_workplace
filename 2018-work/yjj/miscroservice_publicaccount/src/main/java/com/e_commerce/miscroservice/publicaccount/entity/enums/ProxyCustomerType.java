package com.e_commerce.miscroservice.publicaccount.entity.enums;

import lombok.Getter;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 14:52
 * @Copyright 玖远网络
 */
@Getter
public enum ProxyCustomerType{

    /**
     * 市代理
     */
    CITY(1),
    /**
     * 区县代理
     */
    COUNTY(2),
    /**
     * 客户
     */
    CUSTOMER(3);

    private int code;

    ProxyCustomerType(int code) {
        this.code = code;
    }

    public boolean isThis(Integer code) {
        if (code == null) {
            return Boolean.FALSE;
        }
        return code.equals (this.code);
    }

}

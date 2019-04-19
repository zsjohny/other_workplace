package com.e_commerce.miscroservice.publicaccount.entity.enums;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/20 14:52
 * @Copyright 玖远网络
 */
public enum ProxyCustomerAuditStatus{

    /**
     * 成功
     */
    SUCCESS(0),
    /**
     * 处理中
     */
    PROCESSING(1),
    /**
     * 失败
     */
    FAILED(2);
    private int code;

    ProxyCustomerAuditStatus(int code) {
        this.code = code;
    }

    public boolean isThis(Integer code) {
        if (code == null) {
            return Boolean.FALSE;
        }
        return code.equals (this.code);
    }

    public int getCode() {
        return code;
    }
}

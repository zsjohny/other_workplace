/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.enums;

import com.onway.platform.common.enums.EnumBase;

/**
 * @author junjie.lin
 * @version $Id: BizTypeEnum.java, v 0.1 2016/11/24 15:14 junjie.lin Exp $
 */
public enum BizTypeEnum implements EnumBase {

    SIGN_IN("SIGN_IN", "签到", 1610),

    REGISTER_IN("REGISTER_IN", "注册", 2610),

    ORDER_NO("ORDER_NO", "订单编号", 3610),
    
    SERIAL_RECODE("SERIAL_RECODE","流水记录",5610),
    
    REPAY_PLAN("REPAY_PLAN","还款计划",6610),

    ;

    private String code;

    private String message;

    private int    value;

    BizTypeEnum(String code, String message, int value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    public String code() {
        return code;
    }

    /**
     * 获取枚举消息
     *
     * @return
     */
    @Override
    public String message() {
        return message;
    }

    /**
     * 获取枚举值
     *
     * @return
     */
    @Override
    public Number value() {
        return value;
    }
}

package com.store.enumerate;

public enum PaymentType implements IntEnum {
    UNKNOWN(0),

    ALIPAY(1),

    ALIPAY_SDK(2),

    WEIXINPAY_SDK(3),

    WEIXINPAY_NATIVE(4),
    
    BANKCARD_PAY(5);

    private PaymentType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

}

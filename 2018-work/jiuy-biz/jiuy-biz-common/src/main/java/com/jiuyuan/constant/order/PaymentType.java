package com.jiuyuan.constant.order;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 现在在使用 2，3
 * 支付类型枚举类
 * UNKNOWN
 * ALIPAY
 * ALIPAY_SDK：2
 * WEIXINPAY_SDK：3 
 * WEIXINPAY_NATIVE
 * BANKCARD_PAY
 * WEIXINPAY_PUBLIC 公众号
 * WEIXINPAY_WXA 小程序
 * @author zhaoxinglin
 *
 * @version 2017年4月13日上午10:12:01
 */
public enum PaymentType implements IntEnum {
    UNKNOWN(0),

    ALIPAY(1),

    ALIPAY_SDK(2),

    WEIXINPAY_SDK(3),

    WEIXINPAY_NATIVE(4),
    
    BANKCARD_PAY(5),
	 
    WEIXINPAY_PUBLIC(6),
    ACCOUNT_WAIT_MONEY(8),

    WEIXINPAY_WXA(7);

    private PaymentType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

}

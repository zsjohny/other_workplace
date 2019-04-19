package com.jiuyuan.constant.order;

import com.jiuyuan.util.constant.ConstantBinder;

public class OrderConstants {
    static {
        ConstantBinder.bind(OrderConstants.class, ConstantBinder.DEFAULT_CHARSET, "/order_constants.properties");
    }

    public static int PAY_CENTS_PER_UNIT;

    public static int SEND_BACK_PAY_CENTS_PER_UNIT;

}

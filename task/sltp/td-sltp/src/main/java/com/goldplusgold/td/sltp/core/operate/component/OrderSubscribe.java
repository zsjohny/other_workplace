package com.goldplusgold.td.sltp.core.operate.component;

/**
 * 订单的订阅
 * Created by Ness on 2017/5/15.
 */
public interface OrderSubscribe {
    void onMsg(Object msg);
}

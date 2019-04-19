package com.jiuyuan.entity.newentity;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/8/18 17:17
 * @Copyright 玖远网络
 */
public class StoreOrderNewSon extends StoreOrderNew {
    /**
     * 订单细目表
     */
    private Long orderItemId;

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }
}

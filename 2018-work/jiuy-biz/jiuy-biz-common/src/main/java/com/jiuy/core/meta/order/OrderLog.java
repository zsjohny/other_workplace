package com.jiuy.core.meta.order;

import java.io.Serializable;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.BaseMeta;

public class OrderLog extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = -8701127740259286937L;
    
    private long id;
    
    private long userId;
    
    private long orderId;
    
    private OrderStatus oldStatus;
    
    private OrderStatus newStatus;
    
    private long createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(OrderStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getCacheId() {
        return id;
    }

}

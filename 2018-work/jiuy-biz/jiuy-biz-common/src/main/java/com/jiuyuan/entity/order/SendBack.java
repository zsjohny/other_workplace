package com.jiuyuan.entity.order;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class SendBack extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = 3948801833771393650L;

    private long id;

    private long userId;

    private long orderId;

    private long createTime;

    private long updateTime;

    private int status;

    private String expressSupplier;

    private String expressOrderNo;

    private String phone;

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getExpressSupplier() {
        return expressSupplier;
    }

    public void setExpressSupplier(String expressSupplier) {
        this.expressSupplier = expressSupplier;
    }

    public String getExpressOrderNo() {
        return expressOrderNo;
    }

    public void setExpressOrderNo(String expressOrderNo) {
        this.expressOrderNo = expressOrderNo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public Long getCacheId() {
        return id;
    }

}

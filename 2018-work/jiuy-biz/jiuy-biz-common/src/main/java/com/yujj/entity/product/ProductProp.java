package com.yujj.entity.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductProp implements Serializable {

    private static final long serialVersionUID = -3635318269047840284L;

    private long id;

    private long productId;

    private long propertyNameId;

    private long propertyValueId;

//    @JsonIgnore
    private int orderIndex;

//    @JsonIgnore
    private long createTime;

//    @JsonIgnore
    private long updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getPropertyNameId() {
        return propertyNameId;
    }

    public void setPropertyNameId(long propertyNameId) {
        this.propertyNameId = propertyNameId;
    }

    public long getPropertyValueId() {
        return propertyValueId;
    }

    public void setPropertyValueId(long propertyValueId) {
        this.propertyValueId = propertyValueId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
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

    public String getPropKey() {
        return getPropertyNameId() + ":" + getPropertyValueId();
    }

}

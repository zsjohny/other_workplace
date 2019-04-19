package com.jiuyuan.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuy.core.business.assemble.Composable;

public class ProductPropValue implements Serializable ,Composable<Long> {

    private static final long serialVersionUID = -8352649344675273002L;

    private long id;

    private String propertyValue;

    private long propertyNameId;

    @JsonIgnore
    private int status;

    @JsonIgnore
    private int orderIndex;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public long getPropertyNameId() {
        return propertyNameId;
    }

    public void setPropertyNameId(long propertyNameId) {
        this.propertyNameId = propertyNameId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public int hashCode() {
        return ((Long) getId()).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductPropValue) {
            return ((ProductPropValue) obj).getId() == getId();
        }
        return super.equals(obj);
    }

    @Override
	public String toString() {
		return "ProductPropValue [id=" + id + ", propertyValue=" + propertyValue + ", propertyNameId=" + propertyNameId
				+ ", status=" + status + ", orderIndex=" + orderIndex + ", createTime=" + createTime + ", updateTime="
				+ updateTime + "]";
	}
    @Override
    public Long getIdentity() {
        return this.getId();
    }
}
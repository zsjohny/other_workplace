package com.yujj.entity.product;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yujj.entity.product.ProductPropValue;

public class ProductPropName implements Serializable {

    private static final long serialVersionUID = -2001023261560602749L;

    private long id;

    private String propertyName;

//    @JsonIgnore
    private int status;

//    @JsonIgnore
    private int orderIndex;

//    @JsonIgnore
    private long createTime;

//    @JsonIgnore
    private long updateTime;
    
    private List<ProductPropValue> propertyValueList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
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
    public String toString() {
        return getPropertyName();
    }

	public List<ProductPropValue> getPropertyValueList() {
		return propertyValueList;
	}

	public void setPropertyValueList(List<ProductPropValue> propertyValueList) {
		this.propertyValueList = propertyValueList;
	}

    
}

package com.yujj.entity.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Category implements Serializable {

    private static final long serialVersionUID = 4313453248765380535L;

    private long id;

    private String categoryName;

    private long parentId;

    private String iconUrl;

    private String iconOnUrl;

    @JsonIgnore
    private int status;
    
    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private String categoryUrl;
    
    private int isDiscount; 
    
    private double exceedMoney;
    
    private double minusMoney;

    private List<Category> childCategories = new ArrayList<Category>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIconOnUrl() {
        return iconOnUrl;
    }

    public void setIconOnUrl(String iconOnUrl) {
        this.iconOnUrl = iconOnUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    @JsonIgnore
    public List<Long> getCategoryIds() {
        List<Long> categoryIds = new ArrayList<Long>();
        categoryIds.add(getId());
        categoryIds.addAll(getChildCategoryIds());
        return categoryIds;
    }

    @JsonIgnore
    public List<Long> getChildCategoryIds() {
        List<Long> categoryIds = new ArrayList<Long>();
        for (Category category : getChildCategories()) {
            categoryIds.add(category.getId());
            categoryIds.addAll(category.getChildCategoryIds());
        }
        return categoryIds;
    }

	public Map<String, Object> toDiscountMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("id", id);
		map.put("name", categoryName);
		map.put("isDiscount", isDiscount);
		map.put("exceedMoney", exceedMoney);
		map.put("minusMoney", minusMoney);
		
		return map;
	}

	public int getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(int isDiscount) {
		this.isDiscount = isDiscount;
	}

	public double getExceedMoney() {
		return exceedMoney;
	}

	public void setExceedMoney(double exceedMoney) {
		this.exceedMoney = exceedMoney;
	}

	public double getMinusMoney() {
		return minusMoney;
	}

	public void setMinusMoney(double minusMoney) {
		this.minusMoney = minusMoney;
	}
	
}

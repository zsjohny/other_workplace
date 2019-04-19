package com.store.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Brand implements Serializable{
	    
	/**
	 * 
	 */
	private static final long serialVersionUID = 3731910813344197456L;
	
	private String name;
    private String engName;
    private String summary;
    private String iconUrl;
    @JsonIgnore
    private int orderIndex;
	
    @JsonIgnore
    private int id;
	
    private long brandId;
	
	private String brandName;
	
	private String logo;
	
	private String cnName;
	
	private String description;
	
	private int weight;
	
	private String BrandIdentity;
	
    @JsonIgnore
	private int status;
	
    @JsonIgnore
	private long createTime;
	
    @JsonIgnore
	private long updateTime;
    
    private int isDiscount; 
    
    private double exceedMoney;
    
    private double minusMoney;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
    public long getBrandId() {
		return brandId;
	}
	
    public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	
	public String getBrandName() {
		return brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getLogo() {
		return logo;
	}
	
	public void setLogo(String logo) {
		this.logo = logo;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getBrandIdentity() {
		return BrandIdentity;
	}

	public void setBrandIdentity(String brandIdentity) {
		BrandIdentity = brandIdentity;
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

	public Map<String, Object> toSimpleMap() {
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("id", brandId); 
        map.put("brandName", getBrandName());
        map.put("logo", logo);
        map.put("brandIdentity", getBrandIdentity());
		map.put("isDiscount", isDiscount);
		map.put("exceedMoney", exceedMoney);
		map.put("minusMoney", minusMoney);

        return map;
    }
	
	@Override
	public String toString() {
		return "Brand [id=" + id + ", brandId=" + brandId + ", brandName=" + brandName + ", logo=" + logo + ", cnName="
				+ cnName + ", description=" + description + ", weight=" + weight + ", BrandIdentity=" + BrandIdentity
				+ ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
//	@Override
//    public Long getCacheId() {
//        return id;
//    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEngName() {
        return engName;
    }
    public void setEngName(String engName) {
        this.engName = engName;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
   
    public String getIconUrl() {
        return iconUrl;
    }
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
}

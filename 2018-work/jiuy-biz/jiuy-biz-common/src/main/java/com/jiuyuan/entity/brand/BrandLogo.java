package com.jiuyuan.entity.brand;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class BrandLogo extends BaseMeta<Long>{

	public BrandLogo() {
		super();
	}

	public BrandLogo(int id, long brandId, String brandName, String logo, String cnName, String description, int weight,
			String brandIdentity, int brandType, String brandPromotionImg) {
		long time = System.currentTimeMillis();
		
		this.id = id;
		this.brandId = brandId;
		this.brandName = brandName;
		this.logo = logo;
		this.cnName = cnName;
		this.description = description;
		this.weight = weight;
		this.brandIdentity = brandIdentity;
		this.createTime = time;
		this.updateTime = time;
		this.brandType = brandType;
		this.brandPromotionImg = brandPromotionImg;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7531640090878601256L;

	private int id;
	
	private long brandId;
	
	private String brandName;
	
	private int brandType;
	
	private String brandPromotionImg;  
	
	private String logo;
	
	private String cnName;
	
	private String description;
	
	private int weight;
	
	private String brandIdentity;
	
	private int isDiscount;
	
	private double exceedMoney;
	
	private double minusMoney;
	
	private int status;

	private String clothNumberPrefix;
	
	@JsonIgnore
	private long createTime;
	
	@JsonIgnore
	private long updateTime;

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getClothNumberPrefix() {
		return clothNumberPrefix;
	}
	
	public void setClothNumberPrefix(String clothNumberPrefix) {
		this.clothNumberPrefix = clothNumberPrefix;
	}
	
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
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
	
	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
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
		return brandIdentity;
	}

	public void setBrandIdentity(String brandIdentity) {
		this.brandIdentity = brandIdentity;
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

	@Override
	public Long getCacheId() {
		return null;
	}

	public int getBrandType() {
		return brandType;
	}

	public void setBrandType(int brandType) {
		this.brandType = brandType;
	}

	public String getBrandPromotionImg() {
		return brandPromotionImg;
	}

	public void setBrandPromotionImg(String brandPromotionImg) {
		this.brandPromotionImg = brandPromotionImg;
	}
	
}

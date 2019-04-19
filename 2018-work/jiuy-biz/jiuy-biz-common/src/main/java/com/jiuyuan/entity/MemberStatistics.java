package com.jiuyuan.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class MemberStatistics implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 331223153079280851L; 
	
	private long startTime;
	
	private long endTime;

	private long productId;
	
	private String productName;
	
	private int skuId;
	
	private int season;
	
	private int years;
	
	private String brandName;
	
	private String classify;
	
	private String provinceName;
	
	private String cityName;
	
	private String categoryName;
	
	private String categoryId;
	
	private long count;
	
	private BigDecimal money;
	
	public MemberStatistics() {
		
	}

	public MemberStatistics(long startTime, long endTime, long productId, String productName, int skuId, int season, int years,
			String brandName, String classify) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.productId = productId;
		this.productName = productName;
		this.skuId = skuId;
		this.season = season;
		this.years = years;
		this.brandName = brandName;
		this.classify = classify;
	}

	public MemberStatistics(long startCreateTimeL, long endCreateTimeL, long productId2) {
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
		this.productId=productId2;
	}

	public MemberStatistics(long startCreateTimeL, long endCreateTimeL, String provinceName2) {
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
		this.provinceName=provinceName2;
	}

	public MemberStatistics(String cityName, long startCreateTimeL, long endCreateTimeL) {
		this.cityName=cityName;
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
	}

	public MemberStatistics(String categoryName, String categoryId, long startCreateTimeL, long endCreateTimeL) {
		this.categoryName=categoryName;
		this.categoryId=categoryId;
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
	}

	public MemberStatistics(long startCreateTimeL, long endCreateTimeL) {
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
	}

	public MemberStatistics(long count2, BigDecimal money) {
		this.count=count2;
		this.money=money;
	}

	public MemberStatistics(long startCreateTimeL, long endCreateTimeL, long productId2, String productName2) {
		this.startTime=startCreateTimeL;
		this.endTime=endCreateTimeL;
		this.productId=productId2;
		this.productName=productName2;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getName() {
		return productName;
	}

	public void setName(String productName) {
		this.productName = productName;
	}

	public int getSkuId() {
		return skuId;
	}

	public void setSkuId(int skuId) {
		this.skuId = skuId;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getYears() {
		return years;
	}

	public void setYears(int years) {
		this.years = years;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	
}

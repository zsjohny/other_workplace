package com.jiuy.core.meta.brandorder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author jeff.zhan
 * @version 2017年1月3日 上午11:08:30
 * 
 */

public class BrandOrderItem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4307883509124816843L;

	private long id;
	    
    private long orderNo;
    
    private long brandBusinessId;
    
    private long productId;
    
    private long skuId;
    
    private double totalMoney;
    
    private double totalExpressMoney;
    
    private double money;
    
    private double expressMoney;
    
    private int totalUnavalCoinUsed;
    
    private int unavalCoinUsed;

    private int buyCount;

    private String skuSnapshot;

    @JsonIgnore
    private int status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private long brandId;
    
    private long loWarehouseId;
    
    private double totalPay;
    
    private double totalMarketPrice;
    
    private double marketPrice;
    
    private double totalAvailableCommission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public long getBrandBusinessId() {
		return brandBusinessId;
	}

	public void setBrandBusinessId(long brandBusinessId) {
		this.brandBusinessId = brandBusinessId;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public double getTotalExpressMoney() {
		return totalExpressMoney;
	}

	public void setTotalExpressMoney(double totalExpressMoney) {
		this.totalExpressMoney = totalExpressMoney;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getExpressMoney() {
		return expressMoney;
	}

	public void setExpressMoney(double expressMoney) {
		this.expressMoney = expressMoney;
	}

	public int getTotalUnavalCoinUsed() {
		return totalUnavalCoinUsed;
	}

	public void setTotalUnavalCoinUsed(int totalUnavalCoinUsed) {
		this.totalUnavalCoinUsed = totalUnavalCoinUsed;
	}

	public int getUnavalCoinUsed() {
		return unavalCoinUsed;
	}

	public void setUnavalCoinUsed(int unavalCoinUsed) {
		this.unavalCoinUsed = unavalCoinUsed;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public String getSkuSnapshot() {
		return skuSnapshot;
	}

	public void setSkuSnapshot(String skuSnapshot) {
		this.skuSnapshot = skuSnapshot;
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

	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public long getLoWarehouseId() {
		return loWarehouseId;
	}

	public void setLoWarehouseId(long loWarehouseId) {
		this.loWarehouseId = loWarehouseId;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}

	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}

	public double getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(double marketPrice) {
		this.marketPrice = marketPrice;
	}

	public double getTotalAvailableCommission() {
		return totalAvailableCommission;
	}

	public void setTotalAvailableCommission(double totalAvailableCommission) {
		this.totalAvailableCommission = totalAvailableCommission;
	}
    
}

package com.jiuyuan.entity.storeorder;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午5:43:19
*/
public class StoreOrderItem implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1505634844245717589L;

	private long id;
    
    private long orderId;
    
    private long orderNo;
    
    private long storeId;
    
    private long productId;
    
    private long skuId;
    
    private double totalMoney;
    
    private double totalExpressMoney;
    
    private double money;
    
    private double expressMoney;
    
    private int buyCount;

    private String skuSnapshot;

    @JsonIgnore
    private int status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private long brandId;
    
    private long lOWarehouseId;
    
	private double totalPay;
	
	private double totalMarketPrice;
	
	private double marketPrice;
	
	private String position;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
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

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
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
	
	public double getTotalConsume() {
		return this.totalPay + this.totalExpressMoney;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	
}

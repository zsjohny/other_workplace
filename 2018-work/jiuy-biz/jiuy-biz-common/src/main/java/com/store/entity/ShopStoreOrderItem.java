package com.store.entity;

import java.io.Serializable;


import com.yujj.entity.product.ProductSKU;

/**
 * @author jeff.zhan
 * @version 2016年12月2日 上午10:42:48
 * 
 */

public class ShopStoreOrderItem implements Serializable , Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7355299844351931323L;

	private long id;
    
    private long orderNo;
    
    private long orderId;
    
    private long storeId;
    
    private long productId;
    
    private long skuId;
    
    private double totalMoney;
    
    private double totalExpressMoney;
    
    private double money;
    
    private double expressMoney;
    
    private int totalUnavalCoinUsed;
    
    private int unavalCoinUsed;
    
    private int afterSaleFlag;

    private int buyCount;

    private String skuSnapshot;
    
    private String position;
    
    private int status;

    private long createTime;

    private long updateTime;

    private long brandId;
    
    private long lOWarehouseId;
    
    private double totalPay;

    private double totalMarketPrice;
    
    private double marketPrice;
    
    private double totalAvailableCommission;
    
    private long groupId;
    
    private ProductSKU productSKU;
    
    private long supplierId;

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

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getAfterSaleFlag() {
		return afterSaleFlag;
	}

	public void setAfterSaleFlag(int afterSaleFlag) {
		this.afterSaleFlag = afterSaleFlag;
	}

	public ProductSKU getProductSKU() {
		return productSKU;
	}

	public void setProductSKU(ProductSKU productSKU) {
		this.productSKU = productSKU;
	}

	public double getTotalAvailableCommission() {
		return totalAvailableCommission;
	}

	public void setTotalAvailableCommission(double totalAvailableCommission) {
		this.totalAvailableCommission = totalAvailableCommission;
	}
	
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public ShopStoreOrderItem clone() throws CloneNotSupportedException
	{      
		ShopStoreOrderItem cloned = (ShopStoreOrderItem) super.clone();  
	 
	    return cloned; 
	
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	public String toString() {
		return "ShopStoreOrderItem [id=" + id + ", orderNo=" + orderNo + ", orderId=" + orderId + ", storeId=" + storeId
				+ ", productId=" + productId + ", skuId=" + skuId + ", totalMoney=" + totalMoney
				+ ", totalExpressMoney=" + totalExpressMoney + ", money=" + money + ", expressMoney=" + expressMoney
				+ ", totalUnavalCoinUsed=" + totalUnavalCoinUsed + ", unavalCoinUsed=" + unavalCoinUsed
				+ ", afterSaleFlag=" + afterSaleFlag + ", buyCount=" + buyCount + ", skuSnapshot=" + skuSnapshot
				+ ", position=" + position + ", status=" + status + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", brandId=" + brandId + ", lOWarehouseId=" + lOWarehouseId + ", totalPay=" + totalPay
				+ ", totalMarketPrice=" + totalMarketPrice + ", marketPrice=" + marketPrice
				+ ", totalAvailableCommission=" + totalAvailableCommission + ", groupId=" + groupId + ", productSKU="
				+ productSKU + "]";
	}
    
    
}

package com.jiuy.core.meta.brandorder;

import java.io.Serializable;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.constant.order.PaymentTypeDetail;


/**
 * @author jeff.zhan
 * @version 2016年12月8日 下午3:03:25
 * 
 */

public class BrandOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3014526448195556958L;

    private long orderNo;

    private long brandBusinessId;
    
    private int orderType;
    
    private int orderStatus;
    
    private double totalMoney;
    
    private double totalPay;
    
    private double totalExpressMoney;

    private String expressInfo;
    
    private int coinUsed;
    
    private String remark;
    
    private String platform;
    
    private String platformVersion;
    
    private String ip;
    
    private String paymentNo;
    
    private int paymentType;
    
    private long parentId;
    
    private long mergedId;
    
    private long lOWarehouseId;
    
    private int status;
    
    private long createTime;
    
    private long updateTime;
    
    private double totalMarketPrice;
    
    private String cancelReason;
    
    private long pushTime;
    
    private long expiredTime;
    
    private int relatedOrderType;
    
    private long relatedOrderNo;

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

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public double getTotalExpressMoney() {
		return totalExpressMoney;
	}

	public void setTotalExpressMoney(double totalExpressMoney) {
		this.totalExpressMoney = totalExpressMoney;
	}

	public String getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(String expressInfo) {
		this.expressInfo = expressInfo;
	}

	public int getCoinUsed() {
		return coinUsed;
	}

	public void setCoinUsed(int coinUsed) {
		this.coinUsed = coinUsed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(String platformVersion) {
		this.platformVersion = platformVersion;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public int getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public long getMergedId() {
		return mergedId;
	}

	public void setMergedId(long mergedId) {
		this.mergedId = mergedId;
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
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

	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}

	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public long getPushTime() {
		return pushTime;
	}

	public void setPushTime(long pushTime) {
		this.pushTime = pushTime;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public int getRelatedOrderType() {
		return relatedOrderType;
	}

	public void setRelatedOrderType(int relatedOrderType) {
		this.relatedOrderType = relatedOrderType;
	}

	public long getRelatedOrderNo() {
		return relatedOrderNo;
	}

	public void setRelatedOrderNo(long relatedOrderNo) {
		this.relatedOrderNo = relatedOrderNo;
	}
    
    
}

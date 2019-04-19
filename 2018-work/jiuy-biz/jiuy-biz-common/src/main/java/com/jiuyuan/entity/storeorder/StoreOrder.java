package com.jiuyuan.entity.storeorder;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.order.PaymentTypeDetail;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:47:33
*/
public class StoreOrder implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7881611024298308564L;

	private long orderNo;
	
	private long storeId;
	
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
    
    private int status;
    
    private long createTime;

    private long updateTime;
    
    private double totalMarketPrice;
    
    private String cancelReason;
    
    private long pushTime;
    
    private long expiredTime;
    
    private int orderType;
    
    private long loWarehouseId;

    private long payTime;
    private double commission;
    
    private long brandOrder;
    
    private long supplierId;
    
	/**
     * 地推用户id
     */
//	@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
//	@TableField("super_ids")
	private String superIds;
    /**
     * 确认收获日期，格式例：20170909
     */
//	@TableField("confirm_signed_date")
	private Integer confirmSignedDate;
    /**
     * 确认收获日期，格式例：20170909
     */
//	@TableField("confirm_signed_time")
	private Long confirmSignedTime;
	
	/**
     * 限购活动商品id
     */
	private Long restriction_activity_product_id;
	
	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
	}

	public long getLoWarehouseId() {
		return loWarehouseId;
	}

	public void setLoWarehouseId(long loWarehouseId) {
		this.loWarehouseId = loWarehouseId;
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

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	
	public String getOrderSeq() {
    	return String.format("%09d", orderNo);
    }
    
	public String getStoreOrderType() {
    	if(parentId == -1) {
    		return "拆分";
    	} else if (mergedId == -1) {
    		return "组合";
    	}
        return "普通";
    }
    
    public String getCreateTimeString() {
    	if(createTime == 0) {
    		return "";
    	}
    	
    	return DateUtil.convertMSEL(createTime);
    }
    
    public String getUpdateTimeString() {
    	if(updateTime == 0) {
    		return "";
    	}
    	
    	return DateUtil.convertMSEL(updateTime);
    }
    
    public String getPayType() {
    	return PaymentTypeDetail.getByIntValue(paymentType).getShowName();
    }

    public double getPay8Postage() {
    	return new BigDecimal(totalPay + totalExpressMoney).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public long getBrandOrder() {
		return brandOrder;
	}

	public void setBrandOrder(long brandOrder) {
		this.brandOrder = brandOrder;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public Integer getConfirmSignedDate() {
		return confirmSignedDate;
	}

	public void setConfirmSignedDate(Integer confirmSignedDate) {
		this.confirmSignedDate = confirmSignedDate;
	}

	public Long getConfirmSignedTime() {
		return confirmSignedTime;
	}

	public void setConfirmSignedTime(Long confirmSignedTime) {
		this.confirmSignedTime = confirmSignedTime;
	}

	public Long getRestriction_activity_product_id() {
		return restriction_activity_product_id;
	}

	public void setRestriction_activity_product_id(Long restriction_activity_product_id) {
		this.restriction_activity_product_id = restriction_activity_product_id;
	}

}

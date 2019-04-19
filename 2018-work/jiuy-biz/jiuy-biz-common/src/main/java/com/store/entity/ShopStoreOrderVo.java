package com.store.entity;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.DateUtil;
import lombok.Data;


@Data
public class ShopStoreOrderVo implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private int status;
	 private String createTime;
	 private String updateTime;
	 private long expiredTime;
	 private long parentId;
	 private long restriction_activity_product_id;//限购活动商品id
	 private int matchWholesaleLimit;//是否符合混批限制：0不符合、1符合
	 private double TotalPay;
	 private double totalMoney;
	 private double totalExpressMoney;
	    
	    
    private long payTime;
	 private int totalBuyCount;
	 
	//订单对应的orderItem状态
	private String orderItemStatus;
	
	//是否显示售后按钮
    private int isApplyAfterSaleButton;

    //售后订单ID
    private String refundOrderId;

    private long orderNo;
    private String orderNoStr;

    //是否显示平台

	private String type;
    
	/**
	 * 是否启用确认收货按钮
	 * true:禁用
	 * false:启用
	 */
	private boolean disableConfirmationReceipt;
    /**
     * 无售后时自动确认收货时间的毫秒数
     */
    private long autoConfirmTime;
    
    /**
     * 有售后时自动确认收货的暂停时长
     */
    private String autoConfirmTimeString;
    private int orderStatus;
    private String orderStatusName;
	
	 private List<ShopStoreOrderItemNewVO> orderItemsNew;
	private String expressOrderNo;
   private String expressCnName;
   
   
   
   
   public long getParentId() {
	return parentId;
}

public void setParentId(long parentId) {
	this.parentId = parentId;
}

public long getRestriction_activity_product_id() {
	return restriction_activity_product_id;
}

public void setRestriction_activity_product_id(long restriction_activity_product_id) {
	this.restriction_activity_product_id = restriction_activity_product_id;
}
//
//public int getStatus() {
//	return status;
//}
//
//public void setStatus(int status) {
//	this.status = status;
//}


public String getCreateTime() {
	return createTime;
}

public void setCreateTime(String createTime) {
	this.createTime = createTime;
}

public String getUpdateTime() {
	return updateTime;
}

public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
}

public long getExpiredTime() {
	return expiredTime;
}

public void setExpiredTime(long expiredTime) {
	this.expiredTime = expiredTime;
}

public int getMatchWholesaleLimit() {
	return matchWholesaleLimit;
}

public void setMatchWholesaleLimit(int matchWholesaleLimit) {
	this.matchWholesaleLimit = matchWholesaleLimit;
}

public double getTotalPay() {
	return TotalPay;
}

public void setTotalPay(double totalPay) {
	TotalPay = totalPay;
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

public String getOrderStatusName() {
		if(this.getParentId() == -1){
			return "已拆分";
		} else if(OrderStatus.getNameByValue(orderStatus) != null) 
			return OrderStatus.getNameByValue(this.getOrderStatus()).getDisplayName();
		return "";
	}

public String getOrderNoStr() {
		return String.format("%09d", this.getOrderNo());
}
  
public String getAutoConfirmTimeString() {
	return autoConfirmTimeString;
}

public void setAutoConfirmTimeString(String autoConfirmTimeString) {
	this.autoConfirmTimeString = autoConfirmTimeString;
}

public boolean isDisableConfirmationReceipt() {
	return disableConfirmationReceipt;
}

public void setDisableConfirmationReceipt(boolean disableConfirmationReceipt) {
	this.disableConfirmationReceipt = disableConfirmationReceipt;
}

public long getOrderNo() {
	return orderNo;
}

public void setOrderNo(long orderNo) {
	this.orderNo = orderNo;
}

	public String getExpressOrderNo() {
	return expressOrderNo;
}

public void setExpressOrderNo(String expressOrderNo) {
	this.expressOrderNo = expressOrderNo;
}

public String getExpressCnName() {
	return expressCnName;
}

public void setExpressCnName(String expressCnName) {
	this.expressCnName = expressCnName;
}



	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	

	public List<ShopStoreOrderItemNewVO> getOrderItemsNew() {
		return orderItemsNew;
	}

	public void setOrderItemsNew(List<ShopStoreOrderItemNewVO> orderItemsNew) {
		this.orderItemsNew = orderItemsNew;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public int getTotalBuyCount() {
		return totalBuyCount;
	}

	public void setTotalBuyCount(int totalBuyCount) {
		this.totalBuyCount = totalBuyCount;
	}

	public String getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}

	public int getIsApplyAfterSaleButton() {
		return isApplyAfterSaleButton;
	}

	public void setIsApplyAfterSaleButton(int isApplyAfterSaleButton) {
		this.isApplyAfterSaleButton = isApplyAfterSaleButton;
	}

	public String getRefundOrderId() {
		return refundOrderId;
	}

	public void setRefundOrderId(String refundOrderId) {
		this.refundOrderId = refundOrderId;
	}

	

	public long getAutoConfirmTime() {
		return autoConfirmTime;
	}

	public void setAutoConfirmTime(long autoConfirmTime) {
		this.autoConfirmTime = autoConfirmTime;
	}

	
    
    
    
}

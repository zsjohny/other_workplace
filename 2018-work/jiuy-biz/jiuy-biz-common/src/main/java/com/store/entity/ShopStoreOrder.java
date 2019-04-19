package com.store.entity;

import java.io.Serializable;
import java.util.List;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.DateUtil;

/**
 * @author jeff.zhan
 * @version 2016年11月29日 下午8:24:54
 * 
 */

public class ShopStoreOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2910386213263050486L;
	
	/**
     * 无售后按钮
     */
    public static int unApplyAfterSaleButton = 0;
    
    /**
     * 有售后按钮
     */
    public static int applyAfterSaleButton = 1;

    private long orderNo;

	/**
	 * 订单种类 1:商品订单,2购买会员订单
	 */
	private Integer classify;

    private String orderNoAttachmentStr;

    private long storeId;
    
    private int orderType;
    
    private int orderStatus;
    
    private double totalMoney;

    private double TotalPay;

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
    
    private double commission;
    
    private double availableCommission;
    
    private double commissionPercent;
    
    private String cancelReason;
    
    private long pushTime;
    
    private long expiredTime;
    
    private List<ShopStoreOrderItem> orderItems;
    private List<ShopStoreOrderItemNewVO> orderItemsNew;
    
    private List<ShopStoreOrder> childOrderList;
    
    private List<ProductVOShop> productList;

    private List<OrderProduct> orderProducts;
    
    private int totalBuyCount;
    
    private double actualDiscount;
    
    private long payTime;
    
    private int hasWithdrawed;
    
    private long supplierId;
    
    private String expressOrderNo;
    /**
     * 挂起状态
     */
    private int hangUp;
    
//    @JsonIgnore
    private String expressCnName;
    
    /**
     * 地推用户id
     */
//	@TableField("groundUserId")
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
	 * 是否启用确认收货按钮
	 * true:禁用
	 * false:启用
	 */
	private boolean disableConfirmationReceipt;
	
	//是否显示售后按钮
    private int isApplyAfterSaleButton;
    
    //售后订单ID
    private String refundOrderId;
    
    //订单对应的orderItem状态
    private String orderItemStatus;
    
    /**
     * 无售后时自动确认收货时间的毫秒数
     */
    private long autoConfirmTime;
    
    /**
     * 有售后时自动确认收货的暂停时长
     */
    private String autoConfirmTimeString;
    
//    @TableField("SendTime")
    private long SendTime;
    
//    @TableField("auto_take_delivery_pause_time_length")
	private Long auto_take_delivery_pause_time_length;
    
//    @TableField("refund_start_time")
	private Long refund_start_time;
	
	private String express_name;//收件人姓名
	
	private String express_phone;//收件人号码

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

	private String express_address;//收件人地址
    
	private long restriction_activity_product_id;//限购活动商品id
	
	private int matchWholesaleLimit;//是否符合混批限制：0不符合、1符合
	
	public int getHasWithdrawed() {
		return hasWithdrawed;
	}

	public void setHasWithdrawed(int hasWithdrawed) {
		this.hasWithdrawed = hasWithdrawed;
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
		return TotalPay;
	}

	public void setTotalPay(double totalPay) {
		TotalPay = totalPay;
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

	public List<ShopStoreOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<ShopStoreOrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	
	public List<ShopStoreOrderItemNewVO> getOrderItemsNew() {
		return orderItemsNew;
	}

	public void setOrderItemsNew(List<ShopStoreOrderItemNewVO> orderItemsNew) {
		this.orderItemsNew = orderItemsNew;
	}
    
	public String getOrderNoStr() {
		return String.format("%09d", this.getOrderNo());
	}

	public List<ShopStoreOrder> getChildOrderList() {
		return childOrderList;
	}

	public void setChildOrderList(List<ShopStoreOrder> childOrderList) {
		this.childOrderList = childOrderList;
	}
	public String getOrderStatusName() {
		if(this.getParentId() == -1){
			return "已拆分";
		} else if(OrderStatus.getNameByValue(orderStatus) != null) 
			return OrderStatus.getNameByValue(this.getOrderStatus()).getDisplayName();
		return "";
	}
	
	public double getRefundMoney() {
		if(this.getTotalPay() - this.getCommission() >= 0)
			
			return Double.parseDouble(new java.text.DecimalFormat("#.00").format(this.getTotalPay() - this.getCommission()));
		else 
			return  getTotalPay();
	}
	
	public List<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public void setOrderProductList(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public double getAvailableCommission() {
		return availableCommission;
	}

	public void setAvailableCommission(double availableCommission) {
		this.availableCommission = availableCommission;
	}

	public double getCommissionPercent() {
		return commissionPercent;
	}

	public void setCommissionPercent(double commissionPercent) {
		this.commissionPercent = commissionPercent;
	}

	public List<ProductVOShop> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductVOShop> productList) {
		this.productList = productList;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public void setOrderProducts(List<OrderProduct> orderProducts) {
		this.orderProducts = orderProducts;
	}

	public int getTotalBuyCount() {
		return totalBuyCount;
	}

	public void setTotalBuyCount(int totalBuyCount) {
		this.totalBuyCount = totalBuyCount;
	}

	public String getFormatUpdateTime() {
	        return DateUtil.format(getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
	    }
	 
	public String getFormatCreateTime() {
		 return DateUtil.format(getCreateTime(), "yyyy-MM-dd HH:mm:ss");
	 }

	public double getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(double actualDiscount) {
		this.actualDiscount = actualDiscount;
	}

	public long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(long supplierId) {
		this.supplierId = supplierId;
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

	public boolean getDisableConfirmationReceipt() {
		return disableConfirmationReceipt;
	}

	public void setDisableConfirmationReceipt(boolean disableConfirmationReceipt) {
		this.disableConfirmationReceipt = disableConfirmationReceipt;
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

	public String getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}

	public long getAutoConfirmTime() {
		return autoConfirmTime;
	}

	public void setAutoConfirmTime(long autoConfirmTime) {
		this.autoConfirmTime = autoConfirmTime;
	}

	public String getAutoConfirmTimeString() {
		return autoConfirmTimeString;
	}

	public void setAutoConfirmTimeString(String autoConfirmTimeString) {
		this.autoConfirmTimeString = autoConfirmTimeString;
	}

	public long getSendTime() {
		return SendTime;
	}

	public void setSendTime(long sendTime) {
		SendTime = sendTime;
	}

	public Long getAuto_take_delivery_pause_time_length() {
		return auto_take_delivery_pause_time_length;
	}

	public void setAuto_take_delivery_pause_time_length(Long auto_take_delivery_pause_time_length) {
		this.auto_take_delivery_pause_time_length = auto_take_delivery_pause_time_length;
	}

	public Long getRefund_start_time() {
		return refund_start_time;
	}

	public void setRefund_start_time(Long refund_start_time) {
		this.refund_start_time = refund_start_time;
	}

	public String getExpress_name() {
		return express_name;
	}

	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}

	public String getExpress_phone() {
		return express_phone;
	}

	public void setExpress_phone(String express_phone) {
		this.express_phone = express_phone;
	}

	public String getExpress_address() {
		return express_address;
	}

	public void setExpress_address(String express_address) {
		this.express_address = express_address;
	}
	
	public long getRestriction_activity_product_id() {
		return restriction_activity_product_id;
	}

	public long setRestriction_activity_product_id(long restriction_activity_product_id) {
		return this.restriction_activity_product_id = restriction_activity_product_id;
	}

	public int getHangUp() {
		return hangUp;
	}

	public void setHangUp(int hangUp) {
		this.hangUp = hangUp;
	}

	public int getMatchWholesaleLimit() {
		return matchWholesaleLimit;
	}

	public void setMatchWholesaleLimit(int matchWholesaleLimit) {
		this.matchWholesaleLimit = matchWholesaleLimit;
	}

	public String getOrderNoAttachmentStr() {
		return orderNoAttachmentStr;
	}

	public void setOrderNoAttachmentStr(String orderNoAttachmentStr) {
		this.orderNoAttachmentStr = orderNoAttachmentStr;
	}

	

	@Override
	public String toString() {
		return "ShopStoreOrder [orderNo=" + orderNo + ", storeId=" + storeId + ", orderType=" + orderType
				+ ", orderStatus=" + orderStatus + ", totalMoney=" + totalMoney + ", TotalPay=" + TotalPay
				+ ", totalExpressMoney=" + totalExpressMoney + ", expressInfo=" + expressInfo + ", coinUsed=" + coinUsed
				+ ", remark=" + remark + ", platform=" + platform + ", platformVersion=" + platformVersion + ", ip="
				+ ip + ", paymentNo=" + paymentNo + ", paymentType=" + paymentType + ", parentId=" + parentId
				+ ", mergedId=" + mergedId + ", lOWarehouseId=" + lOWarehouseId + ", status=" + status + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", totalMarketPrice=" + totalMarketPrice + ", commission="
				+ commission + ", availableCommission=" + availableCommission + ", commissionPercent="
				+ commissionPercent + ", cancelReason=" + cancelReason + ", pushTime=" + pushTime + ", expiredTime="
				+ expiredTime + ", orderItems=" + orderItems + ", childOrderList=" + childOrderList + ", productList="
				+ productList + ", orderProducts=" + orderProducts + ", totalBuyCount=" + totalBuyCount
				+ ", actualDiscount=" + actualDiscount + ", payTime=" + payTime + ", hasWithdrawed=" + hasWithdrawed
				+ ", supplierId=" + supplierId + ", expressOrderNo=" + expressOrderNo + ", expressCnName="
				+ expressCnName + ", groundUserId=" + groundUserId + ", superIds=" + superIds + ", confirmSignedDate="
				+ confirmSignedDate + ", confirmSignedTime=" + confirmSignedTime + ", disableConfirmationReceipt="
				+ disableConfirmationReceipt + ", isApplyAfterSaleButton=" + isApplyAfterSaleButton + ", refundOrderId="
				+ refundOrderId + ", orderItemStatus=" + orderItemStatus + ", classify=" + classify + "]";
	}

}

package com.yujj.entity.order;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.order.OrderItemGroup;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.PayUtil;

public class Order implements Serializable {

    private static final long serialVersionUID = -459063092155322100L;

    private long id;

    private String orderNo;

    private long userId;

    private OrderType orderType;

    private OrderStatus orderStatus;

    private double totalMoney;

    private double totalExpressMoney;
    
    private String expressInfo;

    private int avalCoinUsed;

    private int unavalCoinUsed;
    
    private int payAmountInCents;



    private String remark;

    @JsonIgnore
    private int status;

    private long createTime;
    
    private long expiredTime;

    @JsonIgnore
    private long updateTime;

    private boolean sended;

    @JsonIgnore
    private String platform;

    @JsonIgnore
    private String platformVersion;

    @JsonIgnore
    private String ip;

    @JsonIgnore
    private String paymentNo;
    //实际折扣金额
    private double actualDiscount;
    //使用的玖币数量
    private int deductCoinNum;

    private List<OrderItem> orderItems;
    
    private List<OrderItemGroup> orderItemGroups;
    //
    private List<OrderDiscountLog> orderDiscountLogs;

    public String getExpressSupplier() {
        return expressSupplier;
    }

    public void setExpressSupplier(String expressSupplier) {
        this.expressSupplier = expressSupplier;
    }

    public String getExpressOrderNo() {
        return expressOrderNo;
    }

    public void setExpressOrderNo(String expressOrderNo) {
        this.expressOrderNo = expressOrderNo;
    }

    /*****add by LWS******/
    private String expressSupplier;
    
    private String expressOrderNo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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

    public String getExpressInfo() {
        return expressInfo;
    }

    public void setExpressInfo(String expressInfo) {
        this.expressInfo = expressInfo;
    }

    public int getAvalCoinUsed() {
        return avalCoinUsed;
    }

    public void setAvalCoinUsed(int avalCoinUsed) {
        this.avalCoinUsed = avalCoinUsed;
    }

    public int getUnavalCoinUsed() {
        return unavalCoinUsed;
    }

    public void setUnavalCoinUsed(int unavalCoinUsed) {
        this.unavalCoinUsed = unavalCoinUsed;
    }

    public int getPayAmountInCents() {
        return payAmountInCents;
    }

    public void setPayAmountInCents(int payAmountInCents) {
        this.payAmountInCents = payAmountInCents;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getFormatUpdateTime() {
        return DateUtil.format(getUpdateTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public boolean isSended() {
        return sended;
    }

    public void setSended(boolean sended) {
        this.sended = sended;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public List<OrderItemGroup> getOrderItemGroups() {
        return orderItemGroups;
    }

    public void setOrderItemGroups(List<OrderItemGroup> orderItemGroups) {
        this.orderItemGroups = orderItemGroups;
    }

    public String getPayMoney() {
        return PayUtil.formatPrice(getPayAmountInCents());
    }
    
	@JsonIgnore
	public double getPrePayment() {
		double totalMoney = 0;
		
		for(OrderItemGroup orderItemGroup : getOrderItemGroups()) {
			totalMoney += orderItemGroup.getTotalMoney();
		}
		
		return totalMoney;
	}

//    public boolean canCancel() {
//        if (this.orderType == OrderType.PAY) {
//            return this.orderStatus == OrderStatus.UNPAID;
//        } else if (this.orderType == OrderType.SEND_BACK) {
//            return this.orderStatus.getIntValue() < OrderStatus.CHECKED.getIntValue();
//        }
//        return false;
//    }

	public List<OrderDiscountLog> getOrderDiscountLogs() {
		return orderDiscountLogs;
	}

	public void setOrderDiscountLogs(List<OrderDiscountLog> orderDiscountLogs) {
		this.orderDiscountLogs = orderDiscountLogs;
	}

	public long getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(long expiredTime) {
		this.expiredTime = expiredTime;
	}

	public double getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(double actualDiscount) {
		this.actualDiscount = actualDiscount;
	}

	public int getDeductCoinNum() {
		return deductCoinNum;
	}

	public void setDeductCoinNum(int deductCoinNum) {
		this.deductCoinNum = deductCoinNum;
	}
	
	
    
}

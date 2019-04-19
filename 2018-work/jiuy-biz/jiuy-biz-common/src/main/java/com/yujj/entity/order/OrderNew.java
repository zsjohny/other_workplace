package com.yujj.entity.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.util.DateUtil;

public class OrderNew implements Serializable {

    private static final long serialVersionUID = -459063092155322100L;


    private long orderNo;

    private long userId;

    private OrderStatus orderStatus;

    private double totalMoney;
    
    private double totalPay;
    

    
    private double totalExpressMoney;
    
    private String expressInfo;

    private int coinUsed;

    private int orderType;

    private String remark;

    @JsonIgnore
    private String platform;

    @JsonIgnore
    private String platformVersion;

    @JsonIgnore
    private String ip;


    private String paymentNo;
    
    @JsonIgnore
    private int paymentType;
    
    private long parentId;
    
    private long mergedId;
    
    @JsonIgnore
    private int status;

    private long createTime;
    
    private long expiredTime;

   
    private long updateTime;
    
    private long lOWarehouseId;
    
    
    private double totalMarketPrice;
    
    private String cancelReason;
    
    private String dividedCommission;

    private double commission;
    
    private double AvailableCommission;

    private long belongBusinessId;
    
    private long distributionStatus;
    
    private int totalBuyCount;
    
    private List<OrderItem> orderItems;
    
    private Map<Long, List<OrderItemVO>> orderItemMap;
    
    private List<List<OrderItemVO>> orderItemSplitList;
    
    private List<OrderNewVO> childOrderList;
    
    
    private List<OrderDiscountLog> orderDiscountLogs;
    
    private long payTime;
    
    /**
     * 获取用户实际支付金额（单位元）
     */
    public double getUserPracticalPayMoneyOfYuan(){
    	return  getTotalPay()+ getTotalExpressMoney();
    }
    
    /**
     * 获取用户实际支付金额（单位分）
     */
    public int getUserPracticalPayMoneyOfFen(){
    	double money =  getUserPracticalPayMoneyOfYuan() *100;
    	return  (int)money;
    }
    
    
    
	public String getCreateTimeStr() {
		if(createTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(createTime, UtilDate.yearMonthDay);
	}
	
	public String getUpdateTimeStr() {
		if(updateTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(updateTime, UtilDate.yearMonthDay);
	}
	
	public String getExpiredTimeStr() {
		if(expiredTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(expiredTime, UtilDate.yearMonthDay);
	}




    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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


	public List<OrderDiscountLog> getOrderDiscountLogs() {
		return orderDiscountLogs;
	}

	public void setOrderDiscountLogs(List<OrderDiscountLog> orderDiscountLogs) {
		this.orderDiscountLogs = orderDiscountLogs;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public int getCoinUsed() {
		return coinUsed;
	}

	public void setCoinUsed(int coinUsed) {
		this.coinUsed = coinUsed;
	}

	public int getPaymentType() {
		return paymentType;
	}
	
	public String getPaymentTypeStr() {
		String paymentStr;
		switch (paymentType) {
			case 1:
			case 2:paymentStr = "支付宝";break;
			case 3:
			case 4:paymentStr = "微信";break;
			case 5:paymentStr = "一网通";break;
			default: paymentStr = "其他";break;
		}
		
		return paymentStr;
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

	public List<OrderNewVO> getChildOrderList() {
		return childOrderList;
	}

	public void setChildOrderList(List<OrderNewVO> childOrderList) {
		this.childOrderList = childOrderList;
	}
	
	public String getOrderNoStr() {
		
			return String.format("%09d", this.getOrderNo());
			
		
	}
	
	public String getOrderStatusName() {
		if(this.getParentId() == -1){
			return "已拆分";
		} else if(getOrderStatus() != null) 
			return OrderStatus.getNameByValue(this.getOrderStatus().getIntValue()).getDisplayName();
		return "";
	}
	public long getPayExpireTime() {
		
		return this.getCreateTime() + 24 * 60 * 60 * 1000;
	}

	public double getTotalMarketPrice() {
		return totalMarketPrice;
	}

	public void setTotalMarketPrice(double totalMarketPrice) {
		this.totalMarketPrice = totalMarketPrice;
	}

	public Map<Long, List<OrderItemVO>> getOrderItemMap() {
		return orderItemMap;
	}

	public void setOrderItemMap(Map<Long, List<OrderItemVO>> orderItemMap) {
		this.orderItemMap = orderItemMap;
	}

	public List<List<OrderItemVO>> getOrderItemSplitList() {
		return orderItemSplitList;
	}

	public void setOrderItemSplitList(List<List<OrderItemVO>> orderItemSplitList) {
		this.orderItemSplitList = orderItemSplitList;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
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

	


	@Override
	public String toString() {
		return "OrderNew [orderNo=" + orderNo + ", userId=" + userId + ", orderStatus=" + orderStatus + ", totalMoney="
				+ totalMoney + ", totalPay=" + totalPay + ", totalExpressMoney=" + totalExpressMoney + ", expressInfo="
				+ expressInfo + ", coinUsed=" + coinUsed + ", orderType=" + orderType + ", remark=" + remark
				+ ", platform=" + platform + ", platformVersion=" + platformVersion + ", ip=" + ip + ", paymentNo="
				+ paymentNo + ", paymentType=" + paymentType + ", parentId=" + parentId + ", mergedId=" + mergedId
				+ ", status=" + status + ", createTime=" + createTime + ", expiredTime=" + expiredTime + ", updateTime="
				+ updateTime + ", lOWarehouseId=" + lOWarehouseId + ", totalMarketPrice=" + totalMarketPrice
				+ ", cancelReason=" + cancelReason + ", commission=" + commission + ", AvailableCommission="
				+ AvailableCommission + ", belongBusinessId=" + belongBusinessId + ", distributionStatus="
				+ distributionStatus + ", totalBuyCount=" + totalBuyCount + ", orderItems=" + orderItems
				+ ", orderItemMap=" + orderItemMap + ", orderItemSplitList=" + orderItemSplitList + ", childOrderList="
				+ childOrderList + ", orderDiscountLogs=" + orderDiscountLogs + ", payTime=" + payTime + "]";
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	public long getBelongBusinessId() {
		return belongBusinessId;
	}

	public void setBelongBusinessId(long belongBusinessId) {
		this.belongBusinessId = belongBusinessId;
	}

	public long getDistributionStatus() {
		return distributionStatus;
	}

	public void setDistributionStatus(long distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public double getAvailableCommission() {
		return AvailableCommission;
	}

	public void setAvailableCommission(double availableCommission) {
		AvailableCommission = availableCommission;
	}

	
	
	public int getTotalBuyCount() {
		return totalBuyCount;
	}

	public void setTotalBuyCount(int totalBuyCount) {
		this.totalBuyCount = totalBuyCount;
	}

	public String getDividedCommission() {
		return dividedCommission;
	}

	public void setDividedCommission(String dividedCommission) {
		this.dividedCommission = dividedCommission;
	}



	
    
}

package com.jiuy.core.meta.order;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.account.User;
import com.jiuy.web.controller.util.DateUtil;

public class OrderDetailVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2873687734567527729L;

	private long orderNo;
	
//	实付总价
	private double totalPay;

	private long createTime;

	private long payTime;
	
	//满减活动优惠
	private List<OrderDiscountLog> orderDiscountLogs;
	
	//支付优惠
	private Double payDiscount;
	
	private double postage;
	
	private String logisticsInfo;
	
	private long splitOrderNo;
	
	private Collection<Long> normalOrderNos;
	
	private long combinationOrderNo;
	
	private String userName;
	
	private String phone;
	
	private long userId;
	
	private String address;
	
	private String credit;

    private Collection<Long> combinationRelativeOrderNos;

    List<OrderItemDetailVO> orderItemDetailVOs;

    List<OrderMessageBoard> messageBoards;

    List<OrderMessageBoard> serviceMessageBoards;

    List<OrderMessageBoard> operationMessageBoards;

    private User user;
    
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
	
	public Double getPayDiscount() {
		return payDiscount;
	}

	public void setPayDiscount(Double payDiscount) {
		this.payDiscount = payDiscount;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getPayTime() {
		return payTime;
	}

	public void setPayTime(long payTime) {
		this.payTime = payTime;
	}

	public double getPostage() {
		return postage;
	}

	public void setPostage(double postage) {
		this.postage = postage;
	}

	public String getLogisticsInfo() {
		return logisticsInfo;
	}

	public void setLogisticsInfo(String logisticsInfo) {
		this.logisticsInfo = logisticsInfo;
	}

	public long getSplitOrderNo() {
		if(hasSplit()) {
			return splitOrderNo;
		}
		return -2;
	}

	public void setSplitOrderNo(long splitOrderNo) {
		this.splitOrderNo = splitOrderNo;
	}

	public Collection<Long> getNormalOrderNos() {
		if(normalOrderNos.size() < 1) {
			return null;
		}
		return normalOrderNos;
	}

	public void setNormalOrderNos(Collection<Long> normalOrderNos) {
		this.normalOrderNos = normalOrderNos;
	}

	public long getCombinationOrderNo() {
		if(hasCombination()) {
			return combinationOrderNo;
		}
		return -2;
	}

	public void setCombinationOrderNo(long combinationOrderNo) {
		this.combinationOrderNo = combinationOrderNo;
	}

	public Collection<Long> getCombinationRelativeOrderNos() {
		if(combinationRelativeOrderNos.size() < 1) {
			return null;
		}
		return combinationRelativeOrderNos;
	}

	public void setCombinationRelativeOrderNos(Collection<Long> combinationRelativeOrderNos) {
		this.combinationRelativeOrderNos = combinationRelativeOrderNos;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<OrderItemDetailVO> getOrderItemDetailVOs() {
		return orderItemDetailVOs;
	}

	public void setOrderItemDetailVOs(List<OrderItemDetailVO> orderItemDetailVOs) {
		this.orderItemDetailVOs = orderItemDetailVOs;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

    public List<OrderMessageBoard> getMessageBoards() {
        return messageBoards;
    }

    public void setMessageBoards(List<OrderMessageBoard> messageBoards) {
        this.messageBoards = messageBoards;
    }

    public List<OrderMessageBoard> getServiceMessageBoards() {
        return serviceMessageBoards;
    }

    public void setServiceMessageBoards(List<OrderMessageBoard> serviceMessageBoards) {
        this.serviceMessageBoards = serviceMessageBoards;
    }

    public List<OrderMessageBoard> getOperationMessageBoards() {
        return operationMessageBoards;
    }

    public void setOperationMessageBoards(List<OrderMessageBoard> operationMessageBoards) {
        this.operationMessageBoards = operationMessageBoards;
    }
    
	public int getTotalBuyCount() {
		int buyCount = 0;
		for(OrderItemDetailVO orderItemDetailVO : orderItemDetailVOs) {
			buyCount += orderItemDetailVO.getBuyCount();
		}
		
		return buyCount;
	}
	
	public String getCreateTimeString() {
		if(createTime <= 0) {
			return "";
		}
		
		return DateUtil.convertMSEL(createTime);
	}
	
	public String getPayTimeString() {
		if(payTime <= 0) {
			return "";
		}
		
		return DateUtil.convertMSEL(payTime);
	}
	
    public List<OrderDiscountLog> getOrderDiscountLogs() {
		return orderDiscountLogs;
	}

	public void setOrderDiscountLogs(List<OrderDiscountLog> orderDiscountLogs) {
		this.orderDiscountLogs = orderDiscountLogs;
	}

	public String getOrderSeq() {
    	return String.format("%09d", orderNo);
    }
	
	public boolean hasSplit() {
		if(splitOrderNo > 0 && splitOrderNo != orderNo) {
			return true;
		}
		
		return false;
	}
	
	public boolean hasCombination() {
		if(combinationOrderNo > 0 && combinationOrderNo != orderNo) {
			return true;
		}
		
		return false;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}

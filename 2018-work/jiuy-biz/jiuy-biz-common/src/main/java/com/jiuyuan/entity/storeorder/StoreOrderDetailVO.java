package com.jiuyuan.entity.storeorder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.newentity.StoreBusiness;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午4:49:34
*/
public class StoreOrderDetailVO {

	private long orderNo;
	
//	实付总价
	private double totalPay;
	private double commission;

	private long createTime;

	private long payTime;
	
	//满减活动优惠
	private List<StoreOrderDiscountLog> storeOrderDiscountLogs;
	
	//支付优惠
	private double payDiscount;
	
	private double postage;
	
	private String logisticsInfo;
	
	private long splitOrderNo;
	
	private Collection<Long> normalOrderNos;
	
	private long combinationOrderNo;
	
	private String userName;
	
	private String phone;
	
	private long storeId;
	
	private String address;
	
	private String credit;

    private Collection<Long> combinationRelativeOrderNos;

    List<StoreOrderItemDetailVO> storeOrderItemDetailVOs;

    List<OrderMessageBoard> messageBoards;

    List<OrderMessageBoard> serviceMessageBoards;

    List<OrderMessageBoard> operationMessageBoards;

    private StoreBusiness storeBusiness;
    
    
    private Double totalMoney;//订单原价
    
    private Double platformPreferential;//平台优惠金额
    
    private Double supplierPreferential;//商家优惠金额
    
    private Double supplierAddPrice;//订单加价金额
    
    private Double totalPayWithoutExpress;//订单待付款原价
    
    private int orderStatus ;//定单状态

    private Map<String, Object> brandOrderInfo = new HashMap<>();
    
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

	public List<StoreOrderDiscountLog> getStoreOrderDiscountLogs() {
		return storeOrderDiscountLogs;
	}

	public void setStoreOrderDiscountLogs(List<StoreOrderDiscountLog> storeOrderDiscountLogs) {
		this.storeOrderDiscountLogs = storeOrderDiscountLogs;
	}

	public Double getPayDiscount() {
		return payDiscount;
	}

	public void setPayDiscount(Double payDiscount) {
		this.payDiscount = payDiscount;
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

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
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

	public List<StoreOrderItemDetailVO> getStoreOrderItemDetailVOs() {
		return storeOrderItemDetailVOs;
	}

	public void setStoreOrderItemDetailVOs(List<StoreOrderItemDetailVO> storeOrderItemDetailVOs) {
		this.storeOrderItemDetailVOs = storeOrderItemDetailVOs;
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

	public StoreBusiness getStoreBusiness() {
		return storeBusiness;
	}

	public void setStoreBusiness(StoreBusiness storeBusiness) {
		this.storeBusiness = storeBusiness;
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
	
	public int getTotalBuyCount() {
		int buyCount = 0;
		for(StoreOrderItemDetailVO storeOrderItemDetailVO : storeOrderItemDetailVOs) {
			buyCount += storeOrderItemDetailVO.getBuyCount();
		}
		
		return buyCount;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}
	
	public double getCanceledMoney() {
		return Math.round((totalPay-commission) * 100)*0.01;
	}

	public Map<String, Object> getBrandOrderInfo() {
		return brandOrderInfo;
	}

	public void setBrandOrderInfo(Map<String, Object> brandOrderInfo) {
		this.brandOrderInfo = brandOrderInfo;
	}

	public void setPayDiscount(double payDiscount) {
		this.payDiscount = payDiscount;
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

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getPlatformPreferential() {
		return platformPreferential;
	}

	public void setPlatformPreferential(Double platformPreferential) {
		this.platformPreferential = platformPreferential;
	}

	public Double getSupplierPreferential() {
		return supplierPreferential;
	}

	public void setSupplierPreferential(Double supplierPreferential) {
		this.supplierPreferential = supplierPreferential;
	}

	public Double getSupplierAddPrice() {
		return supplierAddPrice;
	}

	public void setSupplierAddPrice(Double supplierAddPrice) {
		this.supplierAddPrice = supplierAddPrice;
	}

	public Double getTotalPayWithoutExpress() {
		return totalPayWithoutExpress;
	}

	public void setTotalPayWithoutExpress(Double totalPayWithoutExpress) {
		this.totalPayWithoutExpress = totalPayWithoutExpress;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}

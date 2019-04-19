package com.jiuyuan.entity.storeaftersale;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class StoreServiceTicket extends BaseMeta<Long> implements Serializable {

	   /**
	 * 
	 */
	private static final long serialVersionUID = 349720252343434084L;

 private long id;
	   
 private long orderNo;

 private long OrderItemId;

 private long userId;

 private long skuNo;

 private long businessNumber;

 private int type;

 private int status;

 private long applyTime;

 private int applyReturnCount;

 private double applyReturnMoney;

 private int applyReturnJiuCoin;

 private String applyReturnReason;

 private String applyReturnMemo;

 private String applyImageUrl;

 private long examineTime;

 private String examineMemo;

 private long buyerTime;

 private String buyerExpressCom;

 private String buyerExpressNo;

 private double buyerExpressMoney;

 private long processTime;

 private int processResult;

 private double processMoney;

 private int processReturnJiuCoin;

 private String processReturnMemo;

 private long buyerPayTime;

 private long sellerTime;

 private String sellerExpressCom;

 private String sellerExpressNo;

 private String userRealName;

 private String userTelephone;

 private double sellerExpressMoney;
 
 private double processExpressMoney;

 public StoreServiceTicket() {
	
 }

 public StoreServiceTicket(long orderNo, long nowTime, String applyReturnReson, long orderItemId, long skuNo, long userId, long businessNumber, 
		 int type, int status,int processResult,double processMoney,double processExpressMoney) {
	this.orderNo = orderNo;
	this.applyTime = nowTime;
	this.applyReturnReason = applyReturnReson;
	this.OrderItemId = orderItemId;
	this.skuNo = skuNo;
	this.userId = userId;
	this.businessNumber =businessNumber;
	this.type = type;
	this.status = status;
	this.processResult = processResult;
	this.processMoney = processMoney;
	this.processExpressMoney =processExpressMoney;
 }

@Override
public Long getCacheId() {
	return null;
}

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

public long getOrderItemId() {
	return OrderItemId;
}

public void setOrderItemId(long orderItemId) {
	OrderItemId = orderItemId;
}

public long getUserId() {
	return userId;
}

public void setUserId(long userId) {
	this.userId = userId;
}

public long getSkuNo() {
	return skuNo;
}

public void setSkuNo(long skuNo) {
	this.skuNo = skuNo;
}

public long getBusinessNumber() {
	return businessNumber;
}

public void setBusinessNumber(long businessNumber) {
	this.businessNumber = businessNumber;
}

public int getType() {
	return type;
}

public void setType(int type) {
	this.type = type;
}

public int getStatus() {
	return status;
}

public void setStatus(int status) {
	this.status = status;
}

public long getApplyTime() {
	return applyTime;
}

public void setApplyTime(long applyTime) {
	this.applyTime = applyTime;
}

public int getApplyReturnCount() {
	return applyReturnCount;
}

public void setApplyReturnCount(int applyReturnCount) {
	this.applyReturnCount = applyReturnCount;
}

public double getApplyReturnMoney() {
	return applyReturnMoney;
}

public void setApplyReturnMoney(double applyReturnMoney) {
	this.applyReturnMoney = applyReturnMoney;
}

public int getApplyReturnJiuCoin() {
	return applyReturnJiuCoin;
}

public void setApplyReturnJiuCoin(int applyReturnJiuCoin) {
	this.applyReturnJiuCoin = applyReturnJiuCoin;
}

public String getApplyReturnReason() {
	return applyReturnReason;
}

public void setApplyReturnReason(String applyReturnReason) {
	this.applyReturnReason = applyReturnReason;
}

public String getApplyReturnMemo() {
	return applyReturnMemo;
}

public void setApplyReturnMemo(String applyReturnMemo) {
	this.applyReturnMemo = applyReturnMemo;
}

public String getApplyImageUrl() {
	return applyImageUrl;
}

public void setApplyImageUrl(String applyImageUrl) {
	this.applyImageUrl = applyImageUrl;
}

public long getExamineTime() {
	return examineTime;
}

public void setExamineTime(long examineTime) {
	this.examineTime = examineTime;
}

public String getExamineMemo() {
	return examineMemo;
}

public void setExamineMemo(String examineMemo) {
	this.examineMemo = examineMemo;
}

public long getBuyerTime() {
	return buyerTime;
}

public void setBuyerTime(long buyerTime) {
	this.buyerTime = buyerTime;
}

public String getBuyerExpressCom() {
	return buyerExpressCom;
}

public void setBuyerExpressCom(String buyerExpressCom) {
	this.buyerExpressCom = buyerExpressCom;
}

public String getBuyerExpressNo() {
	return buyerExpressNo;
}

public void setBuyerExpressNo(String buyerExpressNo) {
	this.buyerExpressNo = buyerExpressNo;
}

public double getBuyerExpressMoney() {
	return buyerExpressMoney;
}

public void setBuyerExpressMoney(double buyerExpressMoney) {
	this.buyerExpressMoney = buyerExpressMoney;
}

public long getProcessTime() {
	return processTime;
}

public void setProcessTime(long processTime) {
	this.processTime = processTime;
}

public int getProcessResult() {
	return processResult;
}

public void setProcessResult(int processResult) {
	this.processResult = processResult;
}

public double getProcessMoney() {
	return processMoney;
}

public void setProcessMoney(double processMoney) {
	this.processMoney = processMoney;
}

public int getProcessReturnJiuCoin() {
	return processReturnJiuCoin;
}

public void setProcessReturnJiuCoin(int processReturnJiuCoin) {
	this.processReturnJiuCoin = processReturnJiuCoin;
}

public String getProcessReturnMemo() {
	return processReturnMemo;
}

public void setProcessReturnMemo(String processReturnMemo) {
	this.processReturnMemo = processReturnMemo;
}

public long getBuyerPayTime() {
	return buyerPayTime;
}

public void setBuyerPayTime(long buyerPayTime) {
	this.buyerPayTime = buyerPayTime;
}

public long getSellerTime() {
	return sellerTime;
}

public void setSellerTime(long sellerTime) {
	this.sellerTime = sellerTime;
}

public String getSellerExpressCom() {
	return sellerExpressCom;
}

public void setSellerExpressCom(String sellerExpressCom) {
	this.sellerExpressCom = sellerExpressCom;
}

public String getSellerExpressNo() {
	return sellerExpressNo;
}

public void setSellerExpressNo(String sellerExpressNo) {
	this.sellerExpressNo = sellerExpressNo;
}

public String getUserRealName() {
	return userRealName;
}

public void setUserRealName(String userRealName) {
	this.userRealName = userRealName;
}

public String getUserTelephone() {
	return userTelephone;
}

public void setUserTelephone(String userTelephone) {
	this.userTelephone = userTelephone;
}

public double getSellerExpressMoney() {
	return sellerExpressMoney;
}

public void setSellerExpressMoney(double sellerExpressMoney) {
	this.sellerExpressMoney = sellerExpressMoney;
}

public double getProcessExpressMoney() {
	return processExpressMoney;
}

public void setProcessExpressMoney(double processExpressMoney) {
	this.processExpressMoney = processExpressMoney;
}

}

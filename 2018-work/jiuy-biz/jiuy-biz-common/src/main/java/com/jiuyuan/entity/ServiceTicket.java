package com.jiuyuan.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.Express.ExpressSupport;
import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.store.entity.ShopStoreOrderItem;

public class ServiceTicket  extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = 4733818106411309129L;


	   private long id;
	   
	   private long orderNo;
	   
	   private long orderItemId;
	   
	   
	   
	   private long businessNumber;
	   
	   
	   
	   
	   private int applyReceiveStatus;
	   
	   
	   

	   private long OrderItemId;

	   private long userId;

	   private long skuNo;

	   private long yjjNumber;

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
	   
	   private long processOrderNo;
	   
	   private String buyerMemo;
	   
	   private String sellerMemo;
	   
	   private long exchangeReceivedTime;
	   

	private ShopStoreOrderItem orderItem;
	
	
	public ServiceTicket() {
	}

	 public ServiceTicket(long orderNo, long nowTime, String applyReturnReson, long orderItemId, long skuNo, long userId,long yjjNumber,
			 int type, int status,int processResult,double processMoney,double processExpressMoney) {
		this.orderNo = orderNo;
		this.applyTime = nowTime;
		this.applyReturnReason = applyReturnReson;
		this.OrderItemId = orderItemId;
		this.skuNo = skuNo;
		this.userId = userId;
		this.yjjNumber =yjjNumber;
		this.type = type;
		this.status = status;
		this.processResult = processResult;
		this.processMoney = processMoney;
		this.processExpressMoney =processExpressMoney;
	 }
	
	

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
	}

	

	
	

 public String getExamineTimeString() {
     return DateUtil.convertMSEL(examineTime);
 }

	

 public String getBuyerTimeString() {
     return DateUtil.convertMSEL(buyerTime);
 }

	

	public String getSellerTimeString() {
		return DateUtil.convertMSEL(sellerTime);
	}
	
	
	@Override
	public Long getCacheId() {
		return null;
	}



 public long getBuyerPayTime() {
     return buyerPayTime;
 }
 
 public String getBuyerPayTimeString() {
 	return DateUtil.convertMSEL(buyerPayTime);
 }

 public void setBuyerPayTime(long buyerPayTime) {
     this.buyerPayTime = buyerPayTime;
 }

	public double getProcessExpressMoney() {
		return processExpressMoney;
	}

	public void setProcessExpressMoney(double processExpressMoney) {
		this.processExpressMoney = processExpressMoney;
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
	
	public JSONArray getApplyImageArray() {
    	try {
//    		List<Map<String, Object>> list = JSONArray.parse(getApplyImageUrl(), Map.class);
            JSONArray array = JSON.parseArray(getApplyImageUrl());
            if (array == null) {
                return null;
            }
            return array;
           
		} catch (Exception e) {
			return null;
		}
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

	public double getSellerExpressMoney() {
		return sellerExpressMoney;
	}

	public void setSellerExpressMoney(double sellerExpressMoney) {
		this.sellerExpressMoney = sellerExpressMoney;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public int getApplyReceiveStatus() {
		return applyReceiveStatus;
	}

	public void setApplyReceiveStatus(int applyReceiveStatus) {
		this.applyReceiveStatus = applyReceiveStatus;
	}

	public long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(long orderItemId) {
		this.orderItemId = orderItemId;
	}
    
	public String getStatusName() {
		String name = ServiceTicketStatus.getNameByValue(this.getStatus()).getDisplayName();
		if(getStatus() == 0){
    		if(getType() == 0){
    			name = "退货处理中";    
    		}else{
    			name = "换货处理中";    				
    		}
    	}else if(getStatus() == 1){
    		if(getType() == 0){
    			name = "申请完成，退货申请被驳回";    
    		}else{
    			name = "申请完成，换货申请被驳回";    				
    		}
    	}else if(getStatus() == 2){
    		name = "受理中，等待买家发货退还";    
    		
    	}else if(getStatus() == 4){
    		name = "等待卖家收货";    
    		
    	}else if(getStatus() == 5){
    		if(getProcessResult() == 3){
    			name = "换货处理中";    			
    		}else{
    			name = "待退款";    				
    		}
    	}else if(getStatus() == 6){
    		if(getProcessResult() == 3){
    			name = "已发货";    
    		}else{
    			name = "退款成功";    				
    		}
    	}else if(getStatus() == 7){
    		name = "已收货";    
    	}
		return name;
	}

	public ShopStoreOrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(ShopStoreOrderItem orderItem) {
		this.orderItem = orderItem;
	}
	
	public String getOrderNoStr() {
		
		return String.format("%09d", this.getOrderNo());
	}

	public long getProcessOrderNo() {
		return processOrderNo;
	}
	
	public String getProcessOrderNoStr() {
		
		return String.format("%09d", this.getProcessOrderNo());
	}
	
	public String getIdStr() {
		
		return String.format("%09d", this.getId());
	}
	
	public String getBuyerExpressComStr() {
//		return "身体";
//		return this.getBuyerExpressCom();
		if(this.getBuyerExpressCom() == null)
			return "";
		
		return ExpressSupport.getChnNameByName(this.getBuyerExpressCom().toLowerCase());
	}
	public String getSellerExpressComStr() {
		if(this.getSellerExpressCom() == null)
			return "";
		
		return ExpressSupport.getChnNameByName(this.getSellerExpressCom().toLowerCase());
	}
	
	
	

	public void setProcessOrderNo(long processOrderNo) {
		this.processOrderNo = processOrderNo;
	}

	public String getBuyerMemo() {
		return buyerMemo;
	}

	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}
	
	public String getApplyTimeStr() {
		if(applyTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(applyTime, UtilDate.simple);
	}
	
	public String getExamineTimeStr() {
		if(examineTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(examineTime, UtilDate.simple);
	}
	
	public String getBuyerTimeStr() {
		if(buyerTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(buyerTime, UtilDate.simple);
	}
	
	public String getProcessTimeStr() {
		if(processTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(processTime, UtilDate.simple);
	}
	
	public String getSellerTimeStr() {
		if(sellerTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(sellerTime, UtilDate.simple);
	}
	
	public String getBuyerPayTimeStr() {
		if(buyerPayTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(buyerPayTime, UtilDate.simple);
	}

	public long getExchangeReceivedTime() {
		return exchangeReceivedTime;
	}

	public void setExchangeReceivedTime(long exchangeReceivedTime) {
		this.exchangeReceivedTime = exchangeReceivedTime;
	}
	
	public String getExchangeReceivedTimeStr() {
		if(exchangeReceivedTime <= 0){
			return "";
		}
		return UtilDate.getDateStrFromMillis(exchangeReceivedTime, UtilDate.simple);
	}

	public String getSellerMemo() {
		return sellerMemo;
	}

	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}
	
	
	
	
}
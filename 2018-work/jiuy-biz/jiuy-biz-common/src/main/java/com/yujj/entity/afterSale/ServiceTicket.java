package com.yujj.entity.afterSale;



import java.io.Serializable;

import com.jiuyuan.constant.Express.ExpressSupport;
import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.yujj.entity.order.OrderItem;

public class ServiceTicket implements Serializable {

    private static final long serialVersionUID = 4733818106411309129L;


	   private long id;
	   
	   private long orderNo;
	   
	   private long orderItemId;
	   
	   private long userId;
	   
	   private long skuNo;
	   
	   private long yjjNumber;
	   
	   private int type;
	   
	   private int status;   
	   
	   private long applyTime;
	   
	   private int applyReceiveStatus;
	   
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
	   
	   private long sellerTime;
	   
	   private String sellerExpressCom;
	   
	   private String sellerExpressNo;
	   
	   private String userRealName;
	   
	   private String userTelephone;
	   
	   
	   private double sellerExpressMoney;
	   
	   private long buyerPayTime;
	   
	   private long processOrderNo;
	   
	   private String buyerMemo;
	   
	   private String sellerMemo;
	   
	   private long exchangeReceivedTime;
	   
	   public long getBuyerPayTime() {
		return buyerPayTime;
	}

	public void setBuyerPayTime(long buyerPayTime) {
		this.buyerPayTime = buyerPayTime;
	}

	private OrderItem orderItem;

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

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
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

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
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
//	
//	CREATE TABLE `yjj_ServiceTicket` (
//			  `Id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
//			  `OrderNo` bigint(20) NOT NULL COMMENT '订单号',
//			  `OrderItemId` bigint(20) NOT NULL COMMENT '对应OrderItem的Id',
//			  `SkuNo` bigint(20) NOT NULL COMMENT 'Sku号',
//			  `UserId` bigint(20) NOT NULL,
//			  `UserRealName` varchar(45) DEFAULT NULL,
//			  `UserTelephone` varchar(45) DEFAULT NULL,
//			  `YJJNumber` bigint(20) NOT NULL COMMENT '俞姐姐号',
//			  `Type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '服务类型 0:退货；1:换货',
//			  `Status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:待审核 1:已驳回 2:待买家发货 3:待确认 4:待付款 5:待退款(换货处理中) 6:退款成功(已发货) 7:换货买家确认收货（换货成功）',
//			  `ApplyTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '申请时间',
//			  `ApplyReceiveStatus` tinyint(4) NOT NULL DEFAULT '0' COMMENT '收货状态 1已收货 2 未收货',
//			  `ApplyReturnCount` int(11) NOT NULL DEFAULT '0' COMMENT '申请退换货数量',
//			  `ApplyReturnMoney` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '申请退换货金额',
//			  `ApplyReturnJiuCoin` int(11) NOT NULL DEFAULT '0' COMMENT '申请退换货玖币',
//			  `ApplyReturnReason` varchar(200) NOT NULL COMMENT '申请退换货原因',
//			  `ApplyReturnMemo` text COMMENT '申请退换货说明',
//			  `ApplyImageUrl` varchar(1024) DEFAULT NULL COMMENT '图片 格式[{"url":"http://"},{"url":"http://"}]',
//			  `ExamineTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '审核时间',
//			  `ExamineMemo` text COMMENT '审核说明',
//			  `BuyerTime` bigint(20) DEFAULT '0' COMMENT '买家发货时间',
//			  `BuyerExpressCom` varchar(32) DEFAULT NULL COMMENT '买家发货快递公司',
//			  `BuyerExpressNo` varchar(64) DEFAULT NULL COMMENT '买家发货快递单号',
//			  `BuyerExpressMoney` decimal(10,2) DEFAULT NULL COMMENT '买家发货快递费用',
//			  `BuyerMemo` varchar(45) DEFAULT NULL COMMENT '买家发货字段备注',
//			  `ProcessTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '处理时间',
//			  `ProcessResult` tinyint(4) NOT NULL DEFAULT '0' COMMENT '处理结果 0:未处理, 1:全额退款 2:折价退款 3:换货 4:退款',
//			  `ProcessMoney` decimal(10,2) DEFAULT NULL COMMENT '实际退款金额(换货:实际补差价金额)',
//			  `ProcessExpressMoney` decimal(10,2) DEFAULT NULL,
//			  `ProcessReturnJiuCoin` int(11) NOT NULL DEFAULT '0' COMMENT '处理退换货玖币(换货:实际补差价玖币)',
//			  `ProcessReturnMemo` text COMMENT '处理退换货说明',
//			  `ProcessOrderNo` bigint(20) DEFAULT NULL COMMENT '补差价订单号',
//			  `BuyerPayTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '买家付款时间',
//			  `SellerTime` bigint(20) DEFAULT '0' COMMENT '卖家发货时间',
//			  `SellerExpressCom` varchar(32) DEFAULT NULL COMMENT '卖家发货快递公司',
//			  `SellerExpressNo` varchar(64) DEFAULT NULL COMMENT '卖家发货快递单号',
//			  `SellerExpressMoney` decimal(10,2) DEFAULT NULL COMMENT '卖家发货快递费用',
//			  `SellerMemo` text COMMENT '卖家发货字段备注',
//			  `ExchangeReceivedTime` bigint(20) DEFAULT NULL COMMENT '买家换货确认收货时间',
//			  PRIMARY KEY (`Id`)
//			) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8 COMMENT='服务单表';
//

	
}

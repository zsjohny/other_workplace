package com.store.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotations.TableField;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.DateUtil;


public class ShopStoreOrderInfoVo implements Serializable {
	private static final long serialVersionUID = 1L;

	    
	
	 private long orderNo;
	 private String orderNoStr;
	 private String createTime;
	 
	 private int orderStatus;
	    private String orderStatusName;
	    private long parentId;
		 private int matchWholesaleLimit;//是否符合混批限制：0不符合、1符合
	    private List<Map<String,Object>> orderItems;
		private Long restrictionActivityProductId;//限购活动商品id
		private Double totalExpressMoney;//邮费总价
		private Long expiredTime;//订单过期时间
		
		private Integer hangUp;//是否挂起 0：否  1：是
		private Double totalMoney;//订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）
		private Double totalPay;//订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额
		private String paymentNo;//第三方的支付订单号
	    private String expressInfo;
		
		
		//订单对应的orderItem状态
			private String orderItemStatus;
			//是否显示售后按钮
		    private int isApplyAfterSaleButton;
		    //售后订单ID
		    private String refundOrderId;
		    
		    /**
			 * 是否启用确认收货按钮
			 * true:禁用
			 * false:启用
			 */
			private boolean disableConfirmationReceipt;
		

		public String getExpressInfo() {
			return expressInfo;
		}

		public void setExpressInfo(String expressInfo) {
			this.expressInfo = expressInfo;
		}

		public String getPaymentNo() {
			return paymentNo;
		}

		public void setPaymentNo(String paymentNo) {
			this.paymentNo = paymentNo;
		}

		public Integer getHangUp() {
			return hangUp;
		}

		public void setHangUp(Integer hangUp) {
			this.hangUp = hangUp;
		}

		public Double getTotalMoney() {
			return totalMoney;
		}

		public void setTotalMoney(Double totalMoney) {
			this.totalMoney = totalMoney;
		}

		public Double getTotalPay() {
			return totalPay;
		}

		public void setTotalPay(Double totalPay) {
			this.totalPay = totalPay;
		}

	public Long getExpiredTime() {
			return expiredTime;
		}

		public void setExpiredTime(Long expiredTime) {
			this.expiredTime = expiredTime;
		}

	public Double getTotalExpressMoney() {
			return totalExpressMoney;
		}

		public void setTotalExpressMoney(Double totalExpressMoney) {
			this.totalExpressMoney = totalExpressMoney;
		}

	public Long getRestrictionActivityProductId() {
			return restrictionActivityProductId;
		}

		public void setRestrictionActivityProductId(Long restrictionActivityProductId) {
			this.restrictionActivityProductId = restrictionActivityProductId;
		}

	public int getMatchWholesaleLimit() {
			return matchWholesaleLimit;
		}

		public void setMatchWholesaleLimit(int matchWholesaleLimit) {
			this.matchWholesaleLimit = matchWholesaleLimit;
		}

	public List<Map<String, Object>> getOrderItems() {
			return orderItems;
		}

		public void setOrderItems(List<Map<String, Object>> orderItems) {
			this.orderItems = orderItems;
		}

	public long getParentId() {
			return parentId;
		}

		public void setParentId(long parentId) {
			this.parentId = parentId;
		}

	public int getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(int orderStatus) {
			this.orderStatus = orderStatus;
		}

		public void setOrderNoStr(String orderNoStr) {
			this.orderNoStr = orderNoStr;
		}

		public void setOrderStatusName(String orderStatusName) {
			this.orderStatusName = orderStatusName;
		}

	public String getOrderStatusName() {
			if(this.getParentId() == -1){
				return "已拆分";
			} else if(OrderStatus.getNameByValue(orderStatus) != null) 
				return OrderStatus.getNameByValue(this.getOrderStatus()).getDisplayName();
			return "";
	}
	  
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	
	public String getOrderNoStr() {
			return String.format("%09d", this.getOrderNo());
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
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

	public boolean isDisableConfirmationReceipt() {
		return disableConfirmationReceipt;
	}

	public void setDisableConfirmationReceipt(boolean disableConfirmationReceipt) {
		this.disableConfirmationReceipt = disableConfirmationReceipt;
	}

}

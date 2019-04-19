package com.jiuyuan.service.common;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.RefundStatus;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;

public interface IRefundOrderFacadeNJ {

	
	/**
	 * 买家撤销
	 */
	public void cancelRefundOrder(Long refundOrderId);
    /**
     * 售后订单关闭，通用版
     * @param refundOrderId
     * @param storeId
     * @param refundStatus
     */
	public void updateOrderStatusWhenClose(Long refundOrderId,RefundStatus refundStatus,Long orderNo,RefundOrderActionLogEnum refundOrderActionLogEnum,String platformCloseReason);
	public List<RefundOrder> searchCustomerDeliveryRefundOrder();

	    /**
     * 更改订单状态以及计算自动确认收货总暂停时长
     */
    public void addAutoTakeGeliveryPauseTimeAndClearSign(long orderNo);
	

	
	
}
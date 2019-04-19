package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RefundOrderActionLog;
import com.jiuyuan.entity.newentity.RefundOrderActionLogEnum;
import com.jiuyuan.entity.newentity.StoreOrderNew;

public interface IRefundOrderActionLogService {
	
	/**
	 * 添加售后订单的操作日志
	 * @param refundOrderActionLogEnum
	 */
	public void addActionLog(RefundOrderActionLogEnum refundOrderActionLogEnum,Long refundOrderId);

	/**
	 * 获取售后订单的操作日志列表
	 * @param refundOrderId
	 * @return
	 */
	public List<RefundOrderActionLog> getRefundOrderActionLogList(long refundOrderId) ;

	public void addRefundLog(RefundOrder refundOrder, StoreOrderNew storeOrderNew);
	
	
	
}
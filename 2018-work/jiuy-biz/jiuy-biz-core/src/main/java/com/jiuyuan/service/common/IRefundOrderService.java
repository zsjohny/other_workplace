package com.jiuyuan.service.common;

import java.util.List;

import com.jiuyuan.entity.newentity.RefundOrder;

public interface IRefundOrderService {

	List<RefundOrder> getRefundSuccessListByOrderNo(Long orderNo);

	List<RefundOrder> getRefundOrderWhenSuccess(Long orderNo);

	/**
	 * 获取未结束的售后订单个数
	 * @param supplierId
	 * @return
	 */
	int getUnfinishedRefundOrderCount(long supplierId);
}
package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.util.SmallPage;

public interface IRefundOrderSupplierFacade {

	/**
	 * 申请售后
	 * @param itemId
	 * @param storeId
	 * @param count
	 * @return
	 */
	Map<String, Object> applyRefund(Long itemId, Long storeId, Integer count);

	/**
	 * 获取售后订单详情
	 * @param storeId
	 * @param refundOrderId
	 * @return
	 */
	Map<String, Object> getRefundOrderInfo(long refundOrderId);

	/**
	 * 获得售后订单列表
	 */
	SmallPage getRefundOrderList(long storeId, Page<RefundOrder> page);

	/**
	 * 获取售后订单列表
	 * @param userId
	 * @param refundOrderNo
	 * @param orderNo
	 * @param skuId
	 * @param customerPhone
	 * @param customerName
	 * @param refundStatus
	 * @param refundType
	 * @param refundReasonId
	 * @param refundCostMin
	 * @param refundCostMax
	 * @param returnCountMin
	 * @param returnCountMax
	 * @param applyTimeMin
	 * @param applyTimeMax
	 * @param storeAllowRefundTimeMin
	 * @param storeAllowRefundTimeMax
	 * @param page
	 * @return
	 */
	List<Map<String, Object>> getRefundOrderList(long userId, long refundOrderNo, long orderNo,
			String storePhone, String storeName, int refundStatus, int refundType, String refundReason,
			double refundCostMin, double refundCostMax, int returnCountMin, int returnCountMax, String applyTimeMin,
			String applyTimeMax, String storeDealRefundTimeMin, String storeDealRefundTimeMax,
			Page<Map<String, Object>> page);

	/**
	 * 受理售后订单
	 * @param refundOrderNo
	 * @param dealType
	 * @param dealRemark
	 * @param sendType 
	 * @param verifyCode 
	 * @param phoneNumber 
	 * @return
	 * @throws Exception 
	 */
	int dealRefundOrder(long refundOrderId, int dealType, String dealRemark, String phoneNumber, String verifyCode, String sendType, String receiver, String receiverPhone, String supplierReceiveAddress) throws Exception;
	
	/**
	 * 供应商申请平台介入
	 * @param refundOrderId
	 */
	public void supplierApplyPlatformIntervene(long refundOrderId);

	/**
	 * 卖家确认收货
	 * @param refundOrderNo
	 */
	void confirmReceipt(long refundOrderId);

	/**
	 * 获取售后订单原因列表
	 * @return
	 */
	List<String> getRefundReasonList();

	Map<String, Object> getSupplierDeliveryAddressList(int userId);

}
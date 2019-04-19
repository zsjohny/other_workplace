package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.jiuyuan.web.help.JsonResponse;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.SmallPage;

public interface IRefundOrderFacade {
	
	

	/**
	 * 申请售后
	 * 
	 * @param itemId
	 * @param storeId
	 * @param count
	 * @return
	 */
	Map<String, Object> applyRefund(Long orderNo, Long storeId, Integer refundType,Integer version);

	/**
	 * 获取售后订单详情
	 * 
	 * @param storeId
	 * @param refundOrderId
	 * @return
	 */
	Map<String, Object> getRefundOrderInfo(long refundOrderId);


	/**
	 * 获得售后订单列表
	 */
	SmallPage getRefundOrderList(long storeId,int current,int size);

	/**
	 * 
	 * @param refundType
	 * @param refundReason
	 * @param refundFee
	 * @param itemId
	 * @param productId
	 * @param skuId
	 * @param refundCost
	 * @param returnCount
	 * @param refundRemark
	 * @param refundProofImages
	 * @param refundWay
	 */
	Map<String,Object> submitRefundOrder(Integer refundType, String refundReason, Long refundReasonId, Long orderNo, Double refundCost,
		    String refundRemark, String refundProofImages, Long storeId, String storeName,
			String phone,Integer version);

	/**
	 * 买家发货
	 * @param itemId
	 * @param storeId
	 * @return
	 */
	Map<String, Object> customerDelivery(Long orderNo, Long storeId);

	void submitDelivery(String expressNo, Long expressSupplierId, String expressSupplier, Long storeId,
			Long refundOrderId, String expressSupplierCNName);
	/**
	 * 买家申请平台介入
	 * @param refundOrderId
	 * @param storeId
	 */
	 void storeApplyPlatformIntervene(long refundOrderId, long storeId);



	void checkApplyRefund(Long orderNo, long storeId);
	
	

	 void refundSuccessOperation(Long refundOrderId);
	
	/**
	 * 根据列表售后订单状态返回显示名称
	 * 
	 * @param refundStatus
	 * @return
	 */
	 String buildInfoRefundStatusName(int refundStatus);
	/**
	 * 货物状态
	 */
	 String buildTakeProductStateName(RefundOrder refundOrder);
	/**
	 * 获取关闭理由
	 * 
	 * @param refundOrder
	 * @return
	 */
	 String buildRefundOrderCloseReason(RefundOrder refundOrder) ;
	/**
	 * 剩余卖家确认时间毫秒数
	 * 
	 * @param refundOrder
	 * @return
	 */
	 long buildSurplusAffirmTime(RefundOrder refundOrder);
	/**
	 * 剩余买家发货时间毫秒数
	 * 
	 * @param refundOrder
	 * @return
	 */
	 long buildSurplusDeliverTime(RefundOrder refundOrder);
	
	/**
	 * 剩余卖家自动确认收货时间毫秒数
	 * 
	 * @param refundOrder
	 * @return
	 */
	 long buildSurplusSupplierAutoTakeTime(RefundOrder refundOrder);

	/**
	 *
	 */
	 JsonResponse dealMoney(Long refundOrderId);

}
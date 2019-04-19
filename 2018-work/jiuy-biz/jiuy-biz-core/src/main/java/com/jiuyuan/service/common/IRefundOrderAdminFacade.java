package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.util.SmallPage;

public interface IRefundOrderAdminFacade {

	/**
	 * 获取售后订单详情（新运营平台）
	 * 
	 * @param storeId
	 * @param refundOrderId
	 * @return
	 */
	Map<String, Object> getRefundOrderInfoAdmin(long refundOrderId);
	
	List<Map<String, Object>> getRefundOrderList(Page<Map<String, Object>> page, String refundOrderNo,  String brandName, String receiver, int refundType, int refundStatus, long beginApplyTime, long endApplyTime, int beginReturnCount, int endReturnCount, double beginRefundCost, double endReturnCost, int platformInterveneState, long orderNo2);
	
	/**
	 * 根据订单获取售后订单列表
	 * @param orderNo 订单ID
	 * @return
	 */
	List<Map<String, Object>> getRefundOrderListByOrderNo(long orderNo);
	
	
	/**
	 * 结束平台介入
	 * @param refundOrderId 售后订单ID
	 * @param handlingSuggestion 处理意见
	 * @return
	 */
	void stopPlatformIntervene(long refundOrderId,String handlingSuggestion);
	
	/**
	 * 平台关闭售后单
	 */
	public void platformCloseRefundOrder(long refundOrderId,String platformCloseReason);
}
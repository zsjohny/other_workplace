package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProxyOrder;

public interface IProxyOrderService {

	public List<ProxyOrder> getProxyOrderList( String proxyUserNo,String proxyUserName, String applyFullName, String applyPhone,
			int orderState, 
			Page<Map<String, String>> page);

	public ProxyOrder getProxyOrder(long proxyOrderId);

	public void confirmOrder(long orderId, String applyFullName, int proxyProductCount);
	/**
	 * 完成订单
	 */
	public void finishOrder(long orderId);

	public void closeOrder(long orderId);

	public List<ProxyOrder> searchProxyProductList(long proxyUserId, String applyFullName,
			String applyPhone, int orderState, String proxyProductName, Page<Map<String, String>> page);

	public int getDealingOrderCount(long proxyUserId);

	public int getfinishedOrderCount(long proxyUserId);

	public List<ProxyOrder> selectLatestSituation(long proxyUserId);
	 

}
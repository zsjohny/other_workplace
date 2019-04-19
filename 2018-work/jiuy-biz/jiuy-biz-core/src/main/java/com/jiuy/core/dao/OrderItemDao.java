package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.query.PageQuery;

public interface OrderItemDao {

    List<OrderItem> getByOrderId(long orderId);

    List<OrderItem> getOrderItemGroup(long orderId, long brandId);

	List<OrderItem> getOrderItems(Collection<Long> orderIds);

	List<OrderItem> orderItemsOfGroupIds(Collection<Long> orderGroupIds);

	List<OrderItem> orderItemsOfIds(Collection<Long> orderItemIds);
	
	OrderItem orderItemsOfId(long orderItemId);

	List<OrderItem> orderItemsOfOrderNos(Collection<Long> orderNos);
	
	List<OrderItem> orderItemsOfOrderNo(Long orderNo);

	List<Map<String, Object>> buyCountMapOfOrderNo(Collection<Long> allOrderNos);

    List<OrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> exceptoOrderNos);

	List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos);

	int getBuyCountOfOrderStatus(long userId, Collection<Integer> orderStatus);

    List<OrderItem> orderItemsOfOrderId(Collection<Long> orderIds);

    List<Map<String, Object>> getExpectedSaleCount();
    
    List<OrderItem> orderItemsOfStatisticsId(Collection<Long> statisticsIds, Collection<Long> exceptoOrderNos);

	List<Map<String, Object>> getMonthSales(long startTime, long endTime);

	List<Map<String, Object>> getPerdaySalesVolume(long startTime, long endTime);

	List<Map<String, Object>> getPerdaySalesAmount(long startTime, long endTime);

	int searchOfOrderNos(List<Long> list2);

	List<OrderItem> orderItemsOfProductIds(Collection<Long> productIds);

//	OrderItem searchOfOrderNo(long orderNo);
	Map<Long, Integer> getProductCountMap(Collection<Long> orderNos);

	public List<Map<String, Object>> searchDeductDetailRecord(PageQuery pageQuery,Collection<Long> productIds, Collection<Long> userId,
			long startTime, long endTime);
	
	public int searchDeductDetailRecordCount(Collection<Long> productIds, Collection<Long> userId,
			long startTime, long endTime);
	
}

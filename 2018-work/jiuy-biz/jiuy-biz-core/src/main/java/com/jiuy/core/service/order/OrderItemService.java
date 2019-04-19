package com.jiuy.core.service.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.order.OrderItem;

public interface OrderItemService {
	
	public Map<Long, List<OrderItem>> getOrderItemMapOfGroupIds(Collection<Long> orderGroupIds);

	public Map<Long, OrderItem> OrderItemOfIds(Collection<Long> orderItemIds);
	public OrderItem OrderItemOfId(long orderItemId);

	public Map<Long, List<OrderItem>> OrderItemMapOfOrderNos(Collection<Long> orderNos);

	public List<OrderItem> getOrderItemByOrderNos(Collection<Long> orderNos);

	public Map<Long, Integer> buyCountMapOfOrderNo(Collection<Long> allOrderNos);

    public List<OrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> exceptoOrderNos);

	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos);

	public List<OrderItem> orderItemsOfOrderId(Collection<Long> orderIds);

    public List<OrderItem> orderItemsOfOrderNos(Collection<Long> orderNos);
    
    public List<OrderItem> orderItemsOfStatisticsId(Collection<Long> statisticsIds, Collection<Long> exceptoOrderNos);

	public Map<Long, List<Map<String, Object>>> getPerdaySalesAmount(long startTime, long endTime, Map<String, Double> allMap);

	public Map<Long, List<Map<String, Object>>> getPerdaySalesVolume(long startTime, long endTime,
			Map<String, Integer> allMap);

	public int searchOfOrderNos(List<Long> list2);

	public List<OrderItem> orderItemsOfProductIds(Collection<Long> productIds);

//	public OrderItem searchOfOrderNo(long orderNo);
	public Map<Long, Integer> getProductCountMap(Set<Long> order_nos);
}

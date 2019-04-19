package com.jiuy.core.service.storeorder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午5:41:32
*/
public interface StoreOrderItemService {
	
	public List<StoreOrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> exceptoOrderNos);

	public Map<Long, Integer> buyCountMapOfOrderNo(Collection<Long> allOrderNos);
	
	public Map<Long, StoreOrderItem> OrderItemOfIds(Set<Long> orderItemIds);

	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos);

	public List<StoreOrderItem> getByOrderNo(long orderNo);

	public Map<Long, Integer> getProductCountMap(Collection<Long> orderNos);

	public Map<Long, List<StoreOrderItem>> OrderItemMapOfOrderNos(Collection<Long> orderNos);
}

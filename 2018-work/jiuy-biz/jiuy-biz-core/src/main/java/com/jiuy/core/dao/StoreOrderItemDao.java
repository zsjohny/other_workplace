package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午7:30:16
*/
public interface StoreOrderItemDao {

	List<StoreOrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> orderNos);

	List<Map<String, Object>> buyCountMapOfOrderNo(Collection<Long> allOrderNos);

	List<StoreOrderItem> orderItemsOfIds(Set<Long> orderItemIds);

	List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos);

	int getBuyCountOfOrderStatus(long storeId, Collection<Integer> orderStatus);

	List<StoreOrderItem> orderItemsOfOrderNos(Collection<Long> orderNos);

	List<StoreOrderItem> orderItemsOfIds(List<Long> createList);

	List<StoreOrderItem> getByOrderNo(long orderNo);

	Map<Long, Integer> getProductCountMap(Collection<Long> orderNos);

	List<StoreOrderItem> orderItemsOfProductIds(Collection<Long> productIds);

	List<StoreOrderItem> getOrderItemsBySkuIds(Set<Long> skuIds);
}

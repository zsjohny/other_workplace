package com.jiuy.core.service.order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.meta.order.OrderItem;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	
	@Autowired
	private OrderItemDao orderItemDao;

	@Override
	public Map<Long, List<OrderItem>> getOrderItemMapOfGroupIds(Collection<Long> orderGroupIds) {
		
		if(orderGroupIds.size() < 1) {
			return new HashMap<Long, List<OrderItem>>();
		}
		
		List<OrderItem> orderItems = orderItemDao.orderItemsOfGroupIds(orderGroupIds);
		Map<Long, List<OrderItem>> orMap = new HashMap<Long, List<OrderItem>>();
		
		long lastGroupId = 0;
		List<OrderItem> oItems = null;
		for(OrderItem orderItem : orderItems) {
			if(orderItem.getGroupId() != lastGroupId) {
				oItems = new ArrayList<>();
				lastGroupId = orderItem.getGroupId();
				orMap.put(lastGroupId, oItems);
			}
			oItems.add(orderItem);
		}
		
		return orMap;
	}
	@Override
	public OrderItem OrderItemOfId(long orderItemId) {
		return orderItemDao.orderItemsOfId(orderItemId);
	}
	@Override
	public Map<Long, OrderItem> OrderItemOfIds(Collection<Long> orderItemIds) {
		if(orderItemIds.size() < 1) {
			return new HashMap<Long, OrderItem>();
		}
		
		Map<Long, OrderItem> map = new HashMap<Long, OrderItem>();
		List<OrderItem> orderItems = orderItemDao.orderItemsOfIds(orderItemIds);
		
		for(OrderItem orderItem : orderItems) {
			map.put(orderItem.getId(), orderItem);
		}
		return map;
	}

	@Override
	public Map<Long, List<OrderItem>> OrderItemMapOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new HashMap<Long, List<OrderItem>>();
		}
		
		Map<Long, List<OrderItem>> map = new HashMap<Long, List<OrderItem>>();
		List<OrderItem> orderItems = orderItemDao.orderItemsOfOrderNos(orderNos);
		
		long lastOrderNo = 0;
		List<OrderItem> orderItems2 = null;
		for(OrderItem orderItem : orderItems) {
			if(orderItem.getOrderNo() != lastOrderNo) {
				lastOrderNo = orderItem.getOrderNo();
				orderItems2 = new ArrayList<OrderItem>();
				map.put(lastOrderNo, orderItems2);
			}
			orderItems2.add(orderItem);
		}
		
		return map;
	}

	@Override
	public List<OrderItem> getOrderItemByOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new ArrayList<OrderItem>();
		}
		
		return orderItemDao.orderItemsOfOrderNos(orderNos);
	}

	@Override
	public Map<Long, Integer> buyCountMapOfOrderNo(Collection<Long> allOrderNos) {
		if(allOrderNos.size() < 1) {
			return new HashMap<Long, Integer>();
		}
		
		Map<Long, Integer> buyCountMapOfOrderNo = new HashMap<Long, Integer>();
		List<Map<String, Object>> buyCountMaps = orderItemDao.buyCountMapOfOrderNo(allOrderNos);
		
		for(Map<String, Object> buyCountMap : buyCountMaps) {
			long orderNo = (Long) buyCountMap.get("OrderNo");
			int totalBuyCount = Integer.parseInt(buyCountMap.get("TotalBuyCount").toString());
			buyCountMapOfOrderNo.put(orderNo, totalBuyCount);
		}
		
		return buyCountMapOfOrderNo;
	}

    @Override
    public List<OrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> orderNos) {
        if (skuIds.size() < 1) {
            return new ArrayList<OrderItem>();
        }

        return orderItemDao.orderItemsOfSkuIds(skuIds, orderNos);
    }

	@Override
	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new ArrayList<Map<String, Object>>();
        }

        return orderItemDao.srchSelfParamsOfOrderNos(orderNos);
	}

	@Override
	public List<OrderItem> orderItemsOfOrderId(Collection<Long> orderIds) {
        if (orderIds.size() < 1) {
            return new ArrayList<OrderItem>();
        }

        return orderItemDao.orderItemsOfOrderId(orderIds);
	}

    @Override
    public List<OrderItem> orderItemsOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new ArrayList<OrderItem>();
        }

        return orderItemDao.orderItemsOfOrderNos(orderNos);
    }

	@Override
	public List<OrderItem> orderItemsOfStatisticsId(Collection<Long> statisticsIds, Collection<Long> exceptoOrderNos) {
		return orderItemDao.orderItemsOfStatisticsId(statisticsIds, exceptoOrderNos);
	}

	@Override
	public Map<Long, List<Map<String, Object>>> getPerdaySalesAmount(long startTime, long endTime, Map<String, Double> allMap) {
		List<Map<String, Object>> list = orderItemDao.getPerdaySalesAmount(startTime, endTime);
		
		Map<Long, List<Map<String, Object>>> saleAmountByDay = new HashMap<>();
		List<Map<String, Object>> subList = null;
		Long lasteSkuNo = -1L;
		
		for (Map<String, Object> map : list) {
			String dateTimeStr = map.get("dateTimeStr").toString();
			Double pay = Double.parseDouble(map.get("pay").toString());
			
			Long skuNo = Long.parseLong(map.get("SkuNo").toString());
			if (!skuNo.equals(lasteSkuNo)) {
				lasteSkuNo = skuNo;
				subList = new ArrayList<>();
				saleAmountByDay.put(lasteSkuNo, subList);
			}
			subList.add(map);

			allMap.put(dateTimeStr + skuNo, pay);
		}
		
		return saleAmountByDay;
	}

	@Override
	public Map<Long, List<Map<String, Object>>> getPerdaySalesVolume(long startTime, long endTime,
			Map<String, Integer> allMap) {
		List<Map<String, Object>> list = orderItemDao.getPerdaySalesVolume(startTime, endTime);
		
		Map<Long, List<Map<String, Object>>> saleAmountByDay = new HashMap<>();
		List<Map<String, Object>> subList = null;
		Long lasteSkuNo = -1L;
		
		for (Map<String, Object> map : list) {
			String dateTimeStr = map.get("dateTimeStr").toString();
			Integer volume = Integer.parseInt(map.get("buyCount").toString());
			
			Long skuNo = Long.parseLong(map.get("SkuNo").toString());
			if (!skuNo.equals(lasteSkuNo)) {
				lasteSkuNo = skuNo;
				subList = new ArrayList<>();
				saleAmountByDay.put(lasteSkuNo, subList);
			}
			subList.add(map);

			allMap.put(dateTimeStr + skuNo, volume);
		}
		
		return saleAmountByDay;
	}

	@Override
	public int searchOfOrderNos(List<Long> list2) {
		return orderItemDao.searchOfOrderNos(list2);
	}

	@Override
	public List<OrderItem> orderItemsOfProductIds(Collection<Long> productIds) {
		return orderItemDao.orderItemsOfProductIds(productIds);
	}

	
	
	@Override
	public Map<Long, Integer> getProductCountMap(Set<Long> orderNos) {
		if (orderNos.size() < 1) {
			return new HashMap<>();
		}
		return orderItemDao.getProductCountMap(orderNos);
	}
	
	
}

package com.jiuy.core.service.storeorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreOrderItemDao;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午5:42:09
*/
@Service
public class StoreOrderItemServiceImpl implements StoreOrderItemService {
	
	@Resource
	private StoreOrderItemDao storeOrderItemDao;

	@Override
	public List<StoreOrderItem> orderItemsOfSkuIds(Collection<Long> skuIds, Collection<Long> orderNos) {
		if (skuIds.size() < 1) {
            return new ArrayList<StoreOrderItem>();
        }
		return storeOrderItemDao.orderItemsOfSkuIds(skuIds, orderNos);
	}

	@Override

	public Map<Long, Integer> buyCountMapOfOrderNo(Collection<Long> allOrderNos) {
		if(allOrderNos.size()<1){
			return new HashMap<Long,Integer>();
		}
		Map<Long, Integer> buyCountMapOfOrderNo = new HashMap<Long,Integer>();
		List<Map<String, Object>> buyCountMaps = storeOrderItemDao.buyCountMapOfOrderNo(allOrderNos);
		
		for(Map<String, Object> buyCountMap : buyCountMaps){
			long orderNo = (Long) buyCountMap.get("OrderNo");
			int totalBuyCount = Integer.parseInt(buyCountMap.get("TotalBuyCount").toString());
			buyCountMapOfOrderNo.put(orderNo, totalBuyCount);
		}
		
		return buyCountMapOfOrderNo;
	}
	
	@Override
	public Map<Long, StoreOrderItem> OrderItemOfIds(Set<Long> orderItemIds) {
		if(orderItemIds.size() < 1) {
			return new HashMap<Long, StoreOrderItem>();
		}
		
		Map<Long, StoreOrderItem> map = new HashMap<Long, StoreOrderItem>();
		List<StoreOrderItem> orderItems = storeOrderItemDao.orderItemsOfIds(orderItemIds);
		
		for(StoreOrderItem orderItem : orderItems) {
			map.put(orderItem.getId(), orderItem);
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> srchSelfParamsOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1){
			return new ArrayList<Map<String,Object>>();
		}
		return storeOrderItemDao.srchSelfParamsOfOrderNos(orderNos);
	}

	@Override
	public List<StoreOrderItem> getByOrderNo(long orderNo) {
		return storeOrderItemDao.getByOrderNo(orderNo);
	}

	@Override
	public Map<Long, Integer> getProductCountMap(Collection<Long> orderNos) {
		if (orderNos.size() < 1) {
			return new HashMap<>();
		}
		return storeOrderItemDao.getProductCountMap(orderNos);
	}
	
	@Override
	public Map<Long, List<StoreOrderItem>> OrderItemMapOfOrderNos(Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			return new HashMap<Long, List<StoreOrderItem>>();
		}
		
		Map<Long, List<StoreOrderItem>> map = new HashMap<Long, List<StoreOrderItem>>();
		List<StoreOrderItem> orderItems = storeOrderItemDao.orderItemsOfOrderNos(orderNos);
		
		long lastOrderNo = 0;
		List<StoreOrderItem> orderItems2 = null;
		for(StoreOrderItem orderItem : orderItems) {
			if(orderItem.getOrderNo() != lastOrderNo) {
				lastOrderNo = orderItem.getOrderNo();
				orderItems2 = new ArrayList<StoreOrderItem>();
				map.put(lastOrderNo, orderItems2);
			}
			orderItems2.add(orderItem);
		}
		
		return map;
	}
}

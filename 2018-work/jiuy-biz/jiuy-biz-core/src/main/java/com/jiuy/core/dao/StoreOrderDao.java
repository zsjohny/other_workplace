package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderSO;
import com.jiuyuan.entity.storeorder.StoreOrderVO;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午3:44:12
*/
public interface StoreOrderDao {

	public StoreOrder orderOfNo(String oldOrderNo);

	public List<StoreOrder> searchStoreOrders(PageQuery pageQuery, String orderNo, int orderType, long storeId,
			String receiver, String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos);

	public int searchStoreOrdersCount(String orderNo, int orderType, long storeId, String receiver, String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos);

	public List<StoreOrder> storeOrdersOfSplitOrderNos(Collection<Long> splitOrderNos);

	public List<StoreOrder> storeOrdersOfParentMergedOrderNos(Collection<Long> parentMergedOrderNos);

	public List<StoreOrder> storeOrdersOfOrderNos(Collection<Long> orderNos);

	public List<StoreOrder> childOfSplitOrderNos(Collection<Long> splitOrderNos);
	
	public List<StoreOrder> childOfCombinationOrderNos(Collection<Long> combinationOrderNos);

	public int updateOrderStatus(Collection<Long> orderNos, int orderStatus);

	public StoreOrder storeOrderOfOrderNo(long orderNo);

	public List<StoreOrderVO> searchUndelivered(PageQuery pageQuery, String orderNo, long storeId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos);

	public int searchUndeliveredCount(String orderNo, long storeId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos);

	public StoreOrder orderNewOfOrderNo(long orderNo);

	StoreOrder insert(StoreOrder orderNew2);

	public List<StoreOrder> allUnpaidFacepayOrderNew(long currentTime);

	public List<StoreOrder> getByBrandOrder(long brandOrderNo);

	public Map<Long, StoreOrder> searchUndeliveredMap(StoreOrderSO so, PageQuery pageQuery, Collection<Long> orderNos, List<Long> warehouseIds);

	public int searchUndeliveredCount2(StoreOrderSO so, Collection<Long> orderNos);

	public List<StoreOrder> getByMergedNos(Collection<Long> combineOrderNos);

	public List<StoreOrder> searchSubOrders(long endTime);

	public StoreOrder add(StoreOrder storeOrder);

	public int updateMergedId(Collection<Long> orderNos, long mergedId, long current);

	public int updateSelfMergedId(Collection<Long> orderNos, long current);

	public List<StoreOrder> getOrders(OrderStatus orderStatus);

	public List<StoreOrder> orderNewsOfParentMergedOrderNos(List<Long> asList);

	public int update(long brandOrderNo, long current, long orderNo);

	public List<StoreOrder> ordersOfOrderNos(Collection<Long> orderNos);

	public List<Long> getOrderNosByOrderStatus(int orderStatus, List<Integer> afterSellStatus_list);

	public List<StoreOrder> getByStoreIds(Collection<Long> storeIds);
	
	public List<StoreOrder> selfMergedStoreOrder(long startTime, long endTime);
	
	public List<StoreOrder> getParentMergedStoreOrder(long startTime, long endTime);

	public List<Long> searchStoreOrderFreezeOrders(OrderStatus success, int noWithdraw);

	public int updateHasWithdrawed(long orderNo, int withdraw);

	public int getReferrerAllRctivateCount(String key);

	public int getAreaAllRctivateCount(String key);
}

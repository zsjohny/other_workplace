package com.jiuy.core.service.storeorder;

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
* @version 创建时间: 2016年11月7日 下午4:03:38
*/
public interface StoreOrderService {

	public List<StoreOrder> searchStoreOrders(PageQuery pageQuery, String orderNo, int orderType, long storeId, String receiver,
			String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos);

	int searchStoreOrdersCount(String orderNo, int orderType, long storeId, String receiver, String phone, long startTime,
    		long endTime, int orderStatus, Collection<Long> orderNos);

	public Map<Long, List<StoreOrder>> splitMapOfOrderNos(Collection<Long> splitOrderNos);

	public Map<Long, List<StoreOrder>> parentMergedMap(Collection<Long> combinationOrderNos);

	public List<StoreOrder> childOfSplitOrderNos(Collection<Long> splitOrderNos);
	
	public List<StoreOrder> childOfCombinationOrderNos(Collection<Long> combinationOrderNos);

	public int updateOrderStatus(Collection<Long> orderNos, int orderStatus);

	public List<StoreOrderVO> searchUndelivered(PageQuery pageQuery, String orderNo, long storeId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos);

	public int searchUndeliveredCount(String orderNo, long storeId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos);

	public StoreOrder orderOfOrderNo(long processOrderNo);

	public List<StoreOrder> allUnpaidFacepayOrderNew(long currentTime);

	public void updateOrderStatus(long storeId, long orderNo, int oldStatus, int newStatus, long currentTime);

	public List<StoreOrder> getByBrandOrder(long brandOrderNo);

	public Map<Long, StoreOrder> searchUndeliveredMap(StoreOrderSO so, PageQuery pageQuery,
			Collection<Long> orderNos, List<Long> warehouseIds);

	public int searchUndeliveredCount2(StoreOrderSO so, Set<Long> orderNos);

	public Map<Long, List<StoreOrder>> getMergedChildren(Collection<Long> combineOrderNos);

	public void merge(long endTime);

	public List<StoreOrder> ordersOfOrderNos(Collection<Long> order_nos);

	public List<StoreOrder> getByStoreIds(Collection<Long> storeIds);
	
	public List<StoreOrder> selfMergedStoreOrder(long startTime, long endTime);
	
	public List<StoreOrder> getParentMergedStoreOrder(long startTime, long endTime);
	
	public  Map<Long, StoreOrder> orderNewMapOfOrderNos(Collection<Long> orderNos);

	public List<Long> searchStoreOrderFreezeOrders(OrderStatus success,int noWithdraw);

	public int updateHasWithdrawed(long orderNo, int withdraw);
	
	
	
}

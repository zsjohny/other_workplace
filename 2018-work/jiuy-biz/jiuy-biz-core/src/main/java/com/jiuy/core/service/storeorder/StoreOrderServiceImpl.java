package com.jiuy.core.service.storeorder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.StoreOrderDao;
import com.jiuy.core.dao.StoreOrderLogDao;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;
import com.jiuyuan.entity.storeorder.StoreOrderSO;
import com.jiuyuan.entity.storeorder.StoreOrderVO;
import com.store.dao.mapper.StoreOrderMapper;
import com.store.entity.ShopStoreOrder;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:05:18
*/
@Service
public class StoreOrderServiceImpl implements StoreOrderService {

	@Resource
	private StoreOrderDao storeOrderDao;
	
	@Autowired
	private StoreOrderLogDao storeOrderLogDao;
	

	@Override
	public List<StoreOrder> searchStoreOrders(PageQuery pageQuery, String orderNo, int orderType, long storeId,
			String receiver, String phone, long startTime, long endTime, int orderStatus, Collection<Long> orderNos) {
		if(orderNos.size()<1){
			orderNos = null;
		}
		return storeOrderDao.searchStoreOrders(pageQuery, orderNo, orderType, storeId, receiver, phone, startTime, endTime,
	            orderStatus, orderNos);
	}

	@Override
	public int searchStoreOrdersCount(String orderNo, int orderType, long storeId, String receiver, String phone,
			long startTime, long endTime, int orderStatus, Collection<Long> orderNos) {
		if(orderNos.size() < 1) {
			orderNos = null;
		}
		return storeOrderDao.searchStoreOrdersCount(orderNo, orderType, storeId, receiver, phone, startTime, endTime,
	            orderStatus, orderNos);
	}

	@Override
	public Map<Long, List<StoreOrder>> splitMapOfOrderNos(Collection<Long> splitOrderNos) {
		if(splitOrderNos.size() < 1){
			return new HashMap<Long, List<StoreOrder>>();
		}
		List<StoreOrder> storeOrders = storeOrderDao.storeOrdersOfSplitOrderNos(splitOrderNos);
		
		if(storeOrders.size() < 1){
			return new HashMap<Long, List<StoreOrder>>();
		}
		
		long lastParentId = 0;
		List<StoreOrder> list = null;
		Map<Long, List<StoreOrder>> map = new HashMap<Long, List<StoreOrder>>();
		for(StoreOrder storeOrder : storeOrders){
			long parentId = storeOrder.getParentId();
			if(parentId != lastParentId){
				list = new ArrayList<StoreOrder>();
				lastParentId = parentId;
				map.put(lastParentId, list);
			}
			
			list.add(storeOrder);
		}
		
		return map;
	}

	@Override
	public Map<Long, List<StoreOrder>> parentMergedMap(Collection<Long> parentMergedOrderNos) {
		if(parentMergedOrderNos.size() < 1) {
			return new HashMap<Long, List<StoreOrder>>();
		}
		
		List<StoreOrder> storeOrders = storeOrderDao.storeOrdersOfParentMergedOrderNos(parentMergedOrderNos);

		if(storeOrders.size()<1){
			return new HashMap<Long,List<StoreOrder>>();
		}
		
		long lastMergedId = 0;
        List<StoreOrder> list = null;
        Map<Long, List<StoreOrder>> map = new HashMap<Long, List<StoreOrder>>();
        for(StoreOrder storeOrder : storeOrders){
        	long mergedId = storeOrder.getMergedId();
        	if(mergedId != lastMergedId){
        		list = new ArrayList<StoreOrder>();
        		lastMergedId = mergedId;
        		map.put(lastMergedId, list);
        	}
        	list.add(storeOrder);
        }
		return map;
	}

	@Override
	public List<StoreOrder> childOfSplitOrderNos(Collection<Long> splitOrderNos) {
		if(splitOrderNos.size() < 1){
			return new ArrayList<StoreOrder>();
		}
		return storeOrderDao.childOfSplitOrderNos(splitOrderNos);
	}

	@Override
	public List<StoreOrder> childOfCombinationOrderNos(Collection<Long> combinationOrderNos) {
		if(combinationOrderNos.size() < 1) {
			return new ArrayList<StoreOrder>();
		}
		return storeOrderDao.childOfCombinationOrderNos(combinationOrderNos);
	}

	@Override
	public int updateOrderStatus(Collection<Long> orderNos, int orderStatus) {
		return storeOrderDao.updateOrderStatus(orderNos,orderStatus);
	}

	@Override
	public List<StoreOrderVO> searchUndelivered(PageQuery pageQuery, String orderNo, long storeId, String receiver,
			String phone, long startTime, long endTime, Set<Long> orderNos) {
		if(orderNos.size() < 1){
			orderNos = null;
		}
		return storeOrderDao.searchUndelivered(pageQuery, orderNo, storeId, receiver, phone, startTime, endTime, orderNos);
	}

	@Override
	public int searchUndeliveredCount(String orderNo, long storeId, String receiver, String phone, long startTime,
			long endTime, Set<Long> orderNos) {
		if(orderNos.size() < 1){
			orderNos = null;
		}
		return storeOrderDao.searchUndeliveredCount(orderNo, storeId, receiver, phone, startTime, endTime, orderNos);
	}

	@Override
	public StoreOrder orderOfOrderNo(long processOrderNo) {
		StoreOrder storeOrder =null;
		if(processOrderNo != 0){
			storeOrder = storeOrderDao.orderNewOfOrderNo(processOrderNo);
		}
		return storeOrder;
	}

	@Override
	public List<StoreOrder> allUnpaidFacepayOrderNew(long currentTime) {
		return storeOrderDao.allUnpaidFacepayOrderNew(currentTime);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateOrderStatus(long storeId, long orderNo, int oldStatus, int newStatus, long currentTime) {
		StoreOrderLog1 storeOrderLog = new StoreOrderLog1();
		storeOrderLog.setCreateTime(currentTime);
		storeOrderLog.setNewStatus(newStatus);
		storeOrderLog.setOldStatus(oldStatus);
		storeOrderLog.setOrderNo(orderNo);
		storeOrderLog.setUpdateTime(currentTime);
		storeOrderLog.setStoreId(storeId);
		
		storeOrderLogDao.updateLog(storeId, Arrays.asList(new Long[]{orderNo}), oldStatus, newStatus, currentTime);
		storeOrderDao.updateOrderStatus(Arrays.asList(new Long[]{orderNo}), newStatus);
	}

	@Override
	public List<StoreOrder> getByBrandOrder(long brandOrderNo) {
		return storeOrderDao.getByBrandOrder(brandOrderNo);
	}

	@Override
	public Map<Long, StoreOrder> searchUndeliveredMap(StoreOrderSO so, PageQuery pageQuery, Collection<Long> orderNos, List<Long> warehouseIds) {
		return storeOrderDao.searchUndeliveredMap(so, pageQuery, orderNos, warehouseIds);
	}

	@Override
	public int searchUndeliveredCount2(StoreOrderSO so, Set<Long> orderNos) {
		return storeOrderDao.searchUndeliveredCount2(so, orderNos);
	}

	@Override
	public Map<Long, List<StoreOrder>> getMergedChildren(Collection<Long> combineOrderNos) {
		if (combineOrderNos.size() < 1) {
			return new HashMap<>();
		}
		
		List<StoreOrder> storeOrders = storeOrderDao.getByMergedNos(combineOrderNos);
		Map<Long, List<StoreOrder>> result_map = new HashMap<>();
		List<StoreOrder> list = null;
		long last_key = -1;
		for (StoreOrder storeOrder : storeOrders) {
			if (storeOrder.getMergedId() != last_key) {
				last_key = storeOrder.getMergedId();
				list = new ArrayList<>();
				result_map.put(last_key, list);
			}
			list.add(storeOrder);
		}
		return result_map;
	}

	@Override
	public void merge(long endTime) {
		//封装待合并订单
		Map<Long, List<StoreOrder>> result_map = getHandleMap(endTime);
		long current = System.currentTimeMillis();
		
		Set<Long> normal_order_nos = new HashSet<>();
		for (List<StoreOrder> item_list : result_map.values()) {
			if (item_list.size() == 1) {
				normal_order_nos.add(item_list.get(0).getOrderNo());
			} else if (item_list.size() > 1) {
				createMergerdOrder(item_list, current);
			}
		}
		if (normal_order_nos.size() > 0) {
			storeOrderDao.updateSelfMergedId(normal_order_nos, current);
		}
	}

	private void createMergerdOrder(List<StoreOrder> item_list, long current) {
		StoreOrder storeOrder = new StoreOrder();
		StoreOrder first_order = item_list.get(0);
		storeOrder.setBrandOrder(first_order.getBrandOrder());
		storeOrder.setMergedId(-1L);
		storeOrder.setParentId(0L);
		storeOrder.setOrderStatus(10);
		storeOrder.setOrderType(0);
		storeOrder.setStoreId(first_order.getStoreId());
		storeOrder.setLoWarehouseId(first_order.getLoWarehouseId());
		storeOrder.setExpressInfo(first_order.getExpressInfo());
		storeOrder.setPayTime(first_order.getUpdateTime());
		storeOrder.setCreateTime(current);
		storeOrder.setUpdateTime(current);
		
		double total_money = 0.00;
		double total_pay = 0.00;
		double total_express_money = 0.00;
		int total_coin_used = 0;
		double total_market_price = 0.00;
		Set<Long> sub_order_nos = new HashSet<>();
		for (StoreOrder item : item_list) {
			total_money += item.getTotalMoney();
			total_pay += item.getTotalPay();
			total_express_money += item.getTotalExpressMoney();
			total_coin_used += item.getCoinUsed();
			total_market_price += item.getTotalMarketPrice();
			sub_order_nos.add(item.getOrderNo());
		}
		storeOrder.setTotalMoney(total_money);
		storeOrder.setTotalPay(total_pay);
		storeOrder.setTotalExpressMoney(total_express_money);
		storeOrder.setCoinUsed(total_coin_used);
		storeOrder.setTotalMarketPrice(total_market_price);
		
		StoreOrder sOrder = storeOrderDao.add(storeOrder);
		if (sub_order_nos.size() > 0) {
			storeOrderDao.updateMergedId(sub_order_nos, sOrder.getOrderNo(), current);
		}
	}

	private Map<Long, List<StoreOrder>> getHandleMap(long endTime) {
		Map<Long, List<StoreOrder>> result_map = new HashMap<>();
		List<StoreOrder> storeOrders = storeOrderDao.searchSubOrders(endTime);
		Long key_index = 0L;
        long last_warehouse_id = 0;
        String last_express_info = "";
        List<StoreOrder> list = null;
        for (StoreOrder item : storeOrders) {
            if (item.getLoWarehouseId() != last_warehouse_id || !StringUtils.equals(last_express_info, item.getExpressInfo())) {
            	last_warehouse_id = item.getLoWarehouseId();
            	last_express_info = item.getExpressInfo();
            	list = new ArrayList<StoreOrder>();
                result_map.put(++key_index, list);
            }
            list.add(item);
        }
        
        return result_map;
        
	}

	@Override
	public List<StoreOrder> ordersOfOrderNos(Collection<Long> orderNos) {
		return storeOrderDao.ordersOfOrderNos(orderNos);
	}

	@Override
	public List<StoreOrder> getByStoreIds(Collection<Long> storeIds) {
		return storeOrderDao.getByStoreIds(storeIds);
	}
	

    @Override
    public List<StoreOrder> selfMergedStoreOrder(long startTime, long endTime) {
        return storeOrderDao.selfMergedStoreOrder(startTime, endTime);
    }
    

    @Override
    public List<StoreOrder> getParentMergedStoreOrder(long startTime, long endTime) {
        return storeOrderDao.getParentMergedStoreOrder(startTime, endTime);
    }
	
    @Override
    public Map<Long, StoreOrder> orderNewMapOfOrderNos(Collection<Long> orderNos) {
        if (orderNos.size() < 1) {
            return new HashMap<Long, StoreOrder>();
        }
        List<StoreOrder> orderNews = storeOrderDao.storeOrdersOfOrderNos(orderNos);

        Map<Long, StoreOrder> map = new HashMap<Long, StoreOrder>();
        for (StoreOrder orderNew : orderNews) {
            map.put(orderNew.getOrderNo(), orderNew);
        }

        return map;
    }
    /**
     * 获取确认收货的订单
     */
	@Override
	public List<Long> searchStoreOrderFreezeOrders(OrderStatus success,int noWithdraw) {
		return storeOrderDao.searchStoreOrderFreezeOrders(success,noWithdraw);
	}

	@Override
	public int updateHasWithdrawed(long orderNo, int withdraw) {
		return storeOrderDao.updateHasWithdrawed(orderNo,withdraw);
	}
	
}

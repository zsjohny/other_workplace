package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午9:22:55
*/
public interface StoreOrderLogDao {

	List<StoreOrderLog1> storeOrderLogOfOrderNos(Collection<Long> orderNos);

	List<StoreOrderLog1> storeOrderLogPayOfOrderNos(Collection<Long> orderNos);

	int updateLog(long storeId, Collection<Long> orderNos, int oldOrderSatus, int newOrderStatus, long currentTime);

	List<StoreOrderLog1> getByOrderNos(List<Long> list, OrderStatus deliver, OrderStatus success);

}

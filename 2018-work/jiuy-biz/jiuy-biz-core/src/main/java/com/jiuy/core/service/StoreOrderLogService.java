package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午9:18:39
*/
public interface StoreOrderLogService {

	List<StoreOrderLog1> storeOrderLogOfOrderNos(Collection<Long> orderNos);

	List<StoreOrderLog1> storeOrderLogPayOfOrderNos(Collection<Long> orderNos);

	List<StoreOrderLog1> getByOrderNos(List<Long> list, OrderStatus deliver, OrderStatus success);
}

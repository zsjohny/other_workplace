package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.storeorder.StoreOrderDiscountLog;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午8:04:11
*/
public interface StoreOrderDiscountLogDao {
	List<StoreOrderDiscountLog> getByNo(long orderNo);

	List<StoreOrderDiscountLog> search(Collection<Long> orderNos);
}

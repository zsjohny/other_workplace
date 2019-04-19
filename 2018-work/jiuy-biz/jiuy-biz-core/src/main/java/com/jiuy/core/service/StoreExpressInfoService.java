package com.jiuy.core.service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.jiuyuan.entity.newentity.StoreExpressInfo;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月8日 下午8:19:54
*/
public interface StoreExpressInfoService {

	public Map<Long, StoreExpressInfo> expressInfoMapOfOrderNos(Collection<Long> allOrderNos);

	public int remove(Collection<Long> orderNos);
	
	public int addItem(StoreExpressInfo storeExpressInfo);

}

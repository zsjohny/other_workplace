package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.newentity.StoreExpressInfo;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月8日 下午7:51:03
*/
public interface StoreExpressInfoDao {

	public List<StoreExpressInfo> expressInfoOfOrderNos(Collection<Long> orderNos);

	public List<StoreExpressInfo> expressInfoOfBlurOrderNo(String expressOrderNo);

	public int remove(Collection<Long> orderNos);
	
	public int addItem(StoreExpressInfo storeExpressInfo);

}

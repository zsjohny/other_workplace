package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.storeorder.StoreOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年12月31日 下午10:18:52
 * 
 */

public interface StoreProductDao {

	int insertStoreProduct(List<StoreOrderItem> storeOrderItems);

}

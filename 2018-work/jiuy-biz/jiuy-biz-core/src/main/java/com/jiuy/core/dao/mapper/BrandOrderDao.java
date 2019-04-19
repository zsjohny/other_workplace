package com.jiuy.core.dao.mapper;

import java.util.List;

import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderSO;
import com.jiuy.core.meta.brandorder.BrandOrderUO;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 下午4:14:45
 * 
 */

public interface BrandOrderDao {

	List<BrandOrder> search(BrandOrderSO brandOrderSO);

	BrandOrder add(BrandOrder brandOrder);

	int update(BrandOrderUO uo);

	BrandOrder getByOrderNo(long orderNo);

}

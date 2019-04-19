package com.jiuy.core.dao.mapper;

import java.util.Map;
import java.util.Set;

import com.jiuy.core.meta.brandexpress.BrandExpressInfo;

/**
 * @author jeff.zhan
 * @version 2017年1月9日 下午2:52:22
 * 
 */

public interface BrandExpressInfoDao {

	BrandExpressInfo getByOrderNo(long orderNo);

	int removeByOrderNo(long orderNo);

	Map<Long, BrandExpressInfo> expressInfoMapOfOrderNos(Set<Long> order_nos);

}

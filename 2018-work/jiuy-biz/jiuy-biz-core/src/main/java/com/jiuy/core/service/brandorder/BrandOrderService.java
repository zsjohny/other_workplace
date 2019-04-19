package com.jiuy.core.service.brandorder;

import java.util.List;

import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderSO;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 下午4:14:04
 * 
 */

public interface BrandOrderService {

	List<BrandOrder> search(BrandOrderSO brandOrderSO);

	BrandOrder getByOrderNo(long orderNo);

}

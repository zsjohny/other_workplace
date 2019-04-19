package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.order.OrderDiscountLog;

public interface OrderDiscountLogDao {

	List<OrderDiscountLog> getByNo(long orderNo);

	List<OrderDiscountLog> search(Collection<Long> orderNos);

}

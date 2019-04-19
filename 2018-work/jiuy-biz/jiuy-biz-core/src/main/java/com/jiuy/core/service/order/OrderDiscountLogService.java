package com.jiuy.core.service.order;

import java.util.Collection;
import java.util.List;

import com.jiuy.core.meta.order.OrderDiscountLog;

public interface OrderDiscountLogService {

	List<OrderDiscountLog> search(Collection<Long> orderNos);

}

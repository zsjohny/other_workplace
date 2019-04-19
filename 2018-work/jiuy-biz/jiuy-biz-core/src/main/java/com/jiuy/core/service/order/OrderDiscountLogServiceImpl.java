package com.jiuy.core.service.order;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.OrderDiscountLogDao;
import com.jiuy.core.meta.order.OrderDiscountLog;

@Service
public class OrderDiscountLogServiceImpl implements OrderDiscountLogService {
	
	@Autowired
	private OrderDiscountLogDao orderDiscountLogDao;

	@Override
	public List<OrderDiscountLog> search(Collection<Long> orderNos) {
		return orderDiscountLogDao.search(orderNos);
	}

	
}

package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.OrderDiscountLogDao;
import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.order.OrderDiscountLog;

public class OrderDiscountLogDaoImpl extends SqlSupport implements OrderDiscountLogDao {

	@Override
	public List<OrderDiscountLog> getByNo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNo", orderNo);
		params.put("limit", 1);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderDiscountLogDaoImpl.search", params);
	}

	@Override
	public List<OrderDiscountLog> search(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.OrderDiscountLogDaoImpl.search", params);
	}

}

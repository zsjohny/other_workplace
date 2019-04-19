package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreOrderLogDao;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.storeorder.StoreOrderLog1;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午9:23:30
*/
@Repository
public class StoreOrderLogDaoSqlImpl implements StoreOrderLogDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<StoreOrderLog1> storeOrderLogOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderLogDaoSqlImpl.storeOrderLogOfOrderNos", params);
	}

	@Override
	public List<StoreOrderLog1> storeOrderLogPayOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderLogDaoSqlImpl.storeOrderLogPayOfOrderNos", params);
	}

	@Override
	public int updateLog(long storeId, Collection<Long> orderNos, int oldOrderSatus, int newOrderStatus,
			long currentTime) {
		Map<String, Object> params = new HashMap<>();

        params.put("storeId", storeId);
        params.put("orderNos", orderNos);
        params.put("oldStatus", oldOrderSatus);
        params.put("newStatus", newOrderStatus);
        params.put("createTime", currentTime);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreOrderLogDaoSqlImpl.updateLog", params);
	}

	@Override
	public List<StoreOrderLog1> getByOrderNos(List<Long> list, OrderStatus deliver, OrderStatus success) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", list);
		params.put("oldStatus", deliver.getIntValue());
		params.put("newStatus", success.getIntValue());
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderLogDaoSqlImpl.getByOrderNos", params);
	}

}

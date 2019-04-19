package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreOrderDiscountLogDao;
import com.jiuyuan.entity.storeorder.StoreOrderDiscountLog;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午8:07:21
*/
@Repository
public class StoreOrderDiscountLogDaoSqlImpl implements StoreOrderDiscountLogDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<StoreOrderDiscountLog> getByNo(long orderNo) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNo", orderNo);
		params.put("limit", 1);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDiscountLogDaoSqlImpl.search", params);
	}

	@Override
	public List<StoreOrderDiscountLog> search(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreOrderDiscountLogDaoSqlImpl.search", params);
	}

}

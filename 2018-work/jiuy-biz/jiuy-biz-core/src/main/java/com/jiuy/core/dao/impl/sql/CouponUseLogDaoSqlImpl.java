package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.meta.coupon.CouponUseLog;

@Repository
public class CouponUseLogDaoSqlImpl implements CouponUseLogDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<CouponUseLog> search(Collection<Long> orderNos, Integer status, String sortSQL) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		params.put("status", status);
		params.put("sortSQL", sortSQL);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.CouponUseLogDaoSqlImpl.search", params);
	}

	@Override
	public int add(Collection<CouponUseLog> couponUseLogs) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponUseLogs", couponUseLogs);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.CouponUseLogDaoSqlImpl.add", params);
	}

	@Override
	public int add(CouponUseLog couponUseLog) {
		long time = System.currentTimeMillis();
		couponUseLog.setCreateTime(time);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponUseLog", couponUseLog);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.CouponUseLogDaoSqlImpl.add", params);
	}
	
	
}

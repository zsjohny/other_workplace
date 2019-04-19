package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.CouponUseLogDao;
import com.jiuy.core.dao.StoreCouponUseLogDao;
import com.jiuy.core.meta.coupon.CouponUseLog;
import com.jiuy.core.meta.coupon.StoreCouponUseLog;

@Repository
public class StoreCouponUseLogDaoSqlImpl implements StoreCouponUseLogDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<StoreCouponUseLog> search(Collection<Long> orderNos, Integer status, String sortSQL) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		params.put("status", status);
		params.put("sortSQL", sortSQL);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreCouponUseLogDaoSqlImpl.search", params);
	}

	@Override
	public int add(Collection<StoreCouponUseLog> storeCouponUseLogs) {

		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("storeCouponUseLogs", storeCouponUseLogs);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreCouponUseLogDaoSqlImpl.add", params);
	}

	@Override
	public int add(StoreCouponUseLog storeCouponUseLog) {
		long time = System.currentTimeMillis();
		storeCouponUseLog.setCreateTime(time);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("storeCouponUseLog", storeCouponUseLog);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreCouponUseLogDaoSqlImpl.add", params);
	}

	@Override
	public int updateStoreCouponUseLog(StoreCouponUseLog couponUseLog) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("couponUseLog", couponUseLog);
		
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreCouponUseLogDaoSqlImpl.updateStoreCouponUseLog", params);
	}
	
	
}

package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.brandexpress.BrandExpressInfo;

/**
 * @author jeff.zhan
 * @version 2017年1月9日 下午2:52:40
 * 
 */

@Repository
public class BrandExpressInfoDaoSqlImpl implements BrandExpressInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public BrandExpressInfo getByOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderNo);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.BrandExpressInfoDaoSqlImpl.getByOrderNo", params);
	}

	@Override
	public int removeByOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<>();
		params.put("orderNo", orderNo);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.BrandExpressInfoDaoSqlImpl.removeByOrderNo", params);
	}

	@Override
	public Map<Long, BrandExpressInfo> expressInfoMapOfOrderNos(Set<Long> orderNos) {
		if (orderNos.size() == 0) {
			return new HashMap<>();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.mapper.BrandExpressInfoDaoSqlImpl.expressInfoMapOfOrderNos", params, "orderNo");
	}
	
}

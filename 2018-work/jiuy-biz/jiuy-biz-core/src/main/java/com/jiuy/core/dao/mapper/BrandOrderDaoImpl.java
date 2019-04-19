package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderSO;
import com.jiuy.core.meta.brandorder.BrandOrderUO;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 下午4:14:52
 * 
 */

@Repository
public class BrandOrderDaoImpl implements BrandOrderDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<BrandOrder> search(BrandOrderSO brandOrderSO) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("brandOrderSO", brandOrderSO);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.BrandOrderDaoImpl.search", params);
	}

	@Override
	public BrandOrder add(BrandOrder brandOrder) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.BrandOrderDaoImpl.add", brandOrder);
		return brandOrder;
	}

	@Override
	public int update(BrandOrderUO brandOrderUO) {
		if (brandOrderUO.getOrderNos() == null || brandOrderUO.getOrderNos().size() == 0) {
			return 0;
		}
		Map<String, Object> params = new HashMap<>();
		
		params.put("uo", brandOrderUO);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.BrandOrderDaoImpl.update", params);
	}

	@Override
	public BrandOrder getByOrderNo(long orderNo) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNo", orderNo);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.BrandOrderDaoImpl.getByOrderNo", params);
	}

}

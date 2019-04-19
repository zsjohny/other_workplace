package com.jiuy.core.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SupplierUserDaoImpl implements SupplierUserDao {
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int handleSupplierAvailableBalance(long supplierId, BigDecimal availableBalance) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("id", supplierId);
		params.put("availableBalance", availableBalance);
		return sqlSessionTemplate.update("com.jiuy.core.dao.SupplierUserDaoImpl.handleSupplierAvailableBalance", params);
	}

	@Override
	public BigDecimal getAvailableBalanceById(long supplierId) {
		Map<String,Object> params = new HashMap<String,Object>();
		
		params.put("supplierId", supplierId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.SupplierUserDaoImpl.getAvailableBalanceById", params);
	}
     
}

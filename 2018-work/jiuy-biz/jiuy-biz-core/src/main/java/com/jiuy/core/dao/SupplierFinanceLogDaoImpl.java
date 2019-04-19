package com.jiuy.core.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.newentity.FinanceLogNew;
@Repository
public class SupplierFinanceLogDaoImpl implements SupplierFinanceLogDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int addSupplierFinanceLog(FinanceLogNew supplierFinanceLog) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.SupplierFinanceLogDaoImpl.addSupplierFinanceLog",supplierFinanceLog);
	}

}

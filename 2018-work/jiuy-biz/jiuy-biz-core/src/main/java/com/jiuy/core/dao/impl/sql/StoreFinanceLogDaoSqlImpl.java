package com.jiuy.core.dao.impl.sql;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreFinanceLogDao;
import com.jiuyuan.entity.StoreFinanceLog;
@Repository
public class StoreFinanceLogDaoSqlImpl implements StoreFinanceLogDao{
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void addFinanceLogon(StoreFinanceLog storeFinanceLog) {
		
		sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreFinanceLogDaoSqlImpl.addFinanceLog", storeFinanceLog);
	}

	@Override
	public int updateStoreFinanceLog(StoreFinanceLog storeFinanceLog) {
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreFinanceLogDaoSqlImpl.updateStoreFinanceLog", storeFinanceLog);
	}
}

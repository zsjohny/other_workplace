package com.jiuy.core.dao.impl.sql;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreCommissionPercentageLogDao;
import com.jiuyuan.entity.StoreCommissionPercentageLog;

@Repository
public class StoreCommissionPercentageLogDaoSqlImpl implements StoreCommissionPercentageLogDao{
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int insertLog(StoreCommissionPercentageLog storeCommissionPercentageLog) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreCommissionPercentageLogDaoSqlImpl.insertLog",storeCommissionPercentageLog);
	}
}

package com.jiuy.core.dao.mapper;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.brandorder.BrandOrderLog;

/**
 * @author jeff.zhan
 * @version 2017年1月9日 下午3:24:35
 * 
 */

@Repository
public class BrandOrderLogDaoSqlImpl implements BrandOrderLogDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(BrandOrderLog brandOrderLog) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.BrandOrderLogDaoSqlImpl.add", brandOrderLog);
	}
	
	
	
}

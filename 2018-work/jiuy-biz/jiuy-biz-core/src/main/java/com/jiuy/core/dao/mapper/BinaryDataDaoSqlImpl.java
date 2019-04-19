package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.BinaryData;

@Repository
public class BinaryDataDaoSqlImpl implements BinaryDataDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int add(BinaryData binaryData) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.BinaryDataDaoSqlImpl.add", binaryData);
	}

	@Override
	public BinaryData getById(long id) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.BinaryDataDaoSqlImpl.getById", params);
	}

	@Override
	public BinaryData getWaterMark() {
		Map<String, Object> params = new HashMap<>();
		
		params.put("type", 0);
		params.put("orderSql", " order by id desc");
		params.put("limit", 1);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.BinaryDataDaoSqlImpl.search", params);
	}
	
	
}

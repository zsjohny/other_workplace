package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserVisitHistoryDaoSqlImpl implements UserVisitHistoryDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<Map<String, Object>> searchGroupCount(Collection<Long> relatedIds, int type) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("relatedIds", relatedIds);
		params.put("type", type);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.UserVisitHistoryDaoSqlImpl.searchGroupCount", params);
	}
	
}

package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.SearchKeyword;
import com.jiuyuan.entity.SearchMatchObject;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class SearchDaoSqlImpl implements SearchDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<SearchMatchObject> loadMatchObject() {
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.loadMatchObject");
	}

	@Override
	public int searchCount(String keyword, Integer weightType, Integer minCount, Integer maxCount) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("keyword", keyword);
		params.put("weightType", weightType);
		params.put("minCount", minCount);
		params.put("maxCount", maxCount);
		
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.searchCount", params);
	}

	@Override
	public List<SearchKeyword> search(PageQuery pageQuery, String keyword, Integer weightType, Integer minCount,
			Integer maxCount, Integer sortType) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("pageQuery", pageQuery);
		params.put("keyword", keyword);
		params.put("weightType", weightType);
		params.put("minCount", minCount);
		params.put("maxCount", maxCount);
		params.put("sortType", sortType);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.search", params);
	}

	@Override
	public int update(Long id, Integer weightType, Integer weight) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("id", id);
		params.put("weightType", weightType);
		params.put("weight", weight);
		params.put("updateTime", System.currentTimeMillis());
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.update", params);
	}

	@Override
	public int addKeywords(SearchKeyword searchKeyword) {
		sqlSessionTemplate.update("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.updateKeywords");
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.addKeywords", searchKeyword);
	}

	@Override
	public List<SearchKeyword> search(Integer type) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("type", type);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.search", params);
	}

	@Override
	public int batchAddKeywords(List<SearchKeyword> searchKeywords) {
		sqlSessionTemplate.update("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.updateKeywords");
		
		Map<String, Object> params = new HashMap<>();
		params.put("searchKeywords", searchKeywords);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.SearchDaoSqlImpl.batchAddKeywords", params);
	}
	
	
}
